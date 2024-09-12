/*
 * Copyright (C) 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.prac.paper.web.action.std

import jakarta.servlet.http.Part
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.ems.app.EmsApp
import org.beangle.ems.app.web.WebBusinessLogger
import org.beangle.web.action.context.ActionContext
import org.beangle.web.action.view.{Status, View}
import org.openurp.base.hr.model.Teacher
import org.openurp.base.model.{Project, Semester, User}
import org.openurp.base.std.model.Student
import org.openurp.prac.paper.model.{Paper, PaperCategory, PaperSession, PaperSubject}
import org.openurp.prac.paper.service.PaperService
import org.openurp.starter.web.support.StudentSupport

import java.time.Instant

class IndexAction extends StudentSupport {
  var businessLogger: WebBusinessLogger = _

  var paperService: PaperService = _

  override protected def projectIndex(std: Student): View = {
    given project: Project = std.project

    val semester = getSemester
    put("session", getOpenedSession(project, semester))
    put("papers", paperService.getPapers(std))
    forward()
  }

  def download(): View = {
    val std = getStudent
    val paper = entityDao.get(classOf[Paper], getLongId("paper"))
    if (paper.isWriter(std)) {
      val path = EmsApp.getBlobRepository(true).url(paper.filePath)
      val response = ActionContext.current.response
      response.sendRedirect(path.get.toString)
      null
    } else {
      Status.NotFound
    }
  }

  def applyForm(): View = {
    val std = getStudent
    put("std", std)
    val categories = codeService.get(classOf[PaperCategory])
    put("categories", categories)
    put("subjects", codeService.get(classOf[PaperSubject]))
    val semester = entityDao.get(classOf[Semester], getIntId("semester"))
    val paper = paperService.getPaper(std, semester) match
      case None =>
        val p = new Paper()
        p.semester = semester
        p.category = categories.head
        p.addWriter(1, std)
        p
      case Some(p) => p
    put("paper", paper)
    if (null == paper.mobile) {
      entityDao.findBy(classOf[User], "code" -> std.code, "school" -> std.project.school) foreach { u =>
        paper.mobile = u.mobile.orNull
      }
    }
    forward()
  }

  def update(): View = {
    saveOrUpdate()
  }

  def save(): View = {
    saveOrUpdate()
  }

  def saveOrUpdate(): View = {
    val std = getStudent
    val semester = entityDao.get(classOf[Semester], getIntId("paper.semester"))
    val session = getOpenedSession(std.project, semester)
    if (session.isEmpty) {
      return redirect("index", "申请已经关闭")
    }
    val paper = paperService.getPaper(std, semester).getOrElse(new Paper)
    paper.semester = semester
    paper.mobile = get("paper.mobile", "--")
    paper.title = get("paper.title", "--")
    paper.category = entityDao.get(classOf[PaperCategory], getIntId("paper.category"))
    paper.subject = entityDao.get(classOf[PaperSubject], getIntId("paper.subject"))
    if (paper.subject.name.contains("其他")) {
      paper.subSubjectName = get("paper.subSubjectName")
    }
    get("paper.advisor.id") match
      case None => paper.advisor = None
      case Some(id) =>
        if (id == "null" || Strings.isBlank(id)) paper.advisor = None
        else paper.advisor = Some(entityDao.get(classOf[Teacher], id.toLong))

    val duplicatedStds = Collections.newBuffer[String]
    (1 to 5) foreach { i =>
      getLong(s"writer${i}.std.id") foreach { stdId =>
        val someone = entityDao.get(classOf[Student], stdId)
        val existed = paperService.getPaper(someone, semester)
        if (existed.nonEmpty && !existed.contains(paper)) {
          duplicatedStds.addOne(someone.name)
        } else {
          paper.addWriter(i, someone)
        }
      }
    }
    if (duplicatedStds.nonEmpty) {
      return redirect("index", s"作者多次参与：${duplicatedStds.mkString(",")}")
    }
    val parts = getAll("file", classOf[Part])
    if (parts.nonEmpty && parts.head.getSize > 0) {
      val part = parts.head
      val blob = EmsApp.getBlobRepository(true)
      if (null != paper.filePath && paper.filePath.startsWith("/")) {
        blob.remove(paper.filePath)
      }
      val fileName = part.getSubmittedFileName
      val storeName = s"${std.code}." + Strings.substringAfterLast(fileName, ".")
      val meta = blob.upload("/paper/" + semester.id.toString + "/",
        part.getInputStream, storeName, std.code + " " + std.name)
      paper.filePath = meta.filePath
      paper.submitAt = Instant.now
      paper.fileExt = meta.mediaType
      entityDao.saveOrUpdate(paper)

      val msg = s"上传了学术论文$fileName"
      businessLogger.info(msg, std.id, Map("file" -> fileName))
    } else {
      paper.submitAt = Instant.now
      entityDao.saveOrUpdate(paper)
    }
    redirect("index", "info.save.success")
  }

  def remove(): View = {
    val std = getStudent
    val paper = entityDao.get(classOf[Paper], getLongId("paper"))
    val session = getOpenedSession(std.project, paper.semester)
    if (session.isEmpty) {
      redirect("index", "无法撤销，不在开放时间")
    } else {
      if (paper.isWriter(std)) {
        val blob = EmsApp.getBlobRepository(true)
        if (null != paper.filePath && paper.filePath.startsWith("/")) {
          blob.remove(paper.filePath)
        }
        entityDao.remove(paper)
        businessLogger.info("撤销了学术论文竞赛报名", std.id, Map.empty)
        redirect("index", "撤销成功")
      } else {
        redirect("index", "只能撤销本人的报名")
      }
    }
  }

  private def getOpenedSession(project: Project, semester: Semester): Option[PaperSession] = {
    val sessions = entityDao.findBy(classOf[PaperSession], "project" -> project, "semester" -> semester)
      .filter(s => s.within(Instant.now))
    sessions.headOption
  }

}
