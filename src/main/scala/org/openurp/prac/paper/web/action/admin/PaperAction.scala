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

package org.openurp.prac.paper.web.action.admin

import org.beangle.commons.collection.Collections
import org.beangle.commons.file.zip.Zipper
import org.beangle.commons.io.{Files, IOs}
import org.beangle.commons.lang.{Strings, SystemInfo}
import org.beangle.commons.net.http.HttpUtils.followRedirect
import org.beangle.data.dao.{Condition, OqlBuilder}
import org.beangle.ems.app.EmsApp
import org.beangle.web.action.context.ActionContext
import org.beangle.web.action.view.{Stream, View}
import org.beangle.webmvc.support.action.{ExportSupport, RestfulAction}
import org.openurp.base.model.Project
import org.openurp.prac.paper.model.Paper
import org.openurp.prac.paper.web.helper.StdNamePurifier
import org.openurp.starter.web.support.ProjectSupport

import java.io.{File, FileOutputStream, InputStream, OutputStream}
import java.net.URLConnection

class PaperAction extends RestfulAction[Paper], ProjectSupport, ExportSupport[Paper] {

  override protected def indexSetting(): Unit = {
    given project: Project = getProject

    put("project", project)
    put("semester", getSemester)
  }

  def download(): View = {
    val ids = getLongIds("paper")
    if (ids.length == 1) {
      val paper = entityDao.get(classOf[Paper], ids.head)
      val path = EmsApp.getBlobRepository(true).url(paper.filePath)
      val response = ActionContext.current.response
      response.sendRedirect(path.get.toString)
      null
    } else {
      val papers = entityDao.find(classOf[Paper], ids)
      val semester = papers.head.semester
      val dir = new File(SystemInfo.tmpDir + "/paper/" + semester.id)
      Files.remove(dir)
      dir.mkdirs()
      val blob = EmsApp.getBlobRepository(true)
      papers.foreach { paper =>
        if (paper.filePath.startsWith("/")) {
          blob.url(paper.filePath) foreach { url =>
            val stdName = StdNamePurifier.purify(paper.writerNames)
            val fileName = paper.writerCodes + "_" + stdName + "." + Strings.substringAfterLast(paper.filePath, ".")
            var subjectName = paper.subject.name
            val subSubjectName = paper.subSubjectName.getOrElse("")
            if (Strings.isNotBlank(subSubjectName)) subjectName += ("-" + subSubjectName)
            val subjectDir = new File(dir.getAbsolutePath + s"/${Files.purify(subjectName)}")
            subjectDir.mkdirs()
            val filePath = subjectDir.getAbsolutePath + Files./ + fileName
            downloading(url.openConnection(), new File(filePath))
          }
        }
      }
      val targetZip = new File(SystemInfo.tmpDir + "/paper/paper.zip")
      Zipper.zip(dir, targetZip)
      val fileName = semester.schoolYear + "_" + semester.name + " 学术论文竞赛" + s"${papers.size}篇.zip"
      Stream(targetZip, "application/zip", fileName)
    }
  }

  private def downloading(c: URLConnection, location: File): Unit = {
    val conn = followRedirect(c, "GET")
    var input: InputStream = null
    var output: OutputStream = null
    try {
      val file = new File(location.toString + ".part")
      file.delete()
      val buffer = Array.ofDim[Byte](1024 * 4)
      input = conn.getInputStream
      output = new FileOutputStream(file)
      var n = input.read(buffer)
      while (-1 != n) {
        output.write(buffer, 0, n)
        n = input.read(buffer)
      }
      //先关闭文件读写，再改名
      IOs.close(input, output)
      input = null
      output = null
      file.renameTo(location)
    } catch {
      case e: Throwable =>
        logger.warn(s"Cannot download file ${location}")
    }
    finally {
      IOs.close(input, output)
    }
  }

  override protected def getQueryBuilder: OqlBuilder[Paper] = {
    val query = super.getQueryBuilder
    val code = get("writer.std.code")
    val name = get("writer.std.name")
    val departId = getInt("writer.std.state.department.id")

    if (code.nonEmpty || name.nonEmpty || departId.nonEmpty) {
      val params = Collections.newBuffer[Any]
      val q = Collections.newBuffer[String]
      if (code.nonEmpty && !code.get.isBlank) {
        code foreach { c =>
          val codes = Strings.split(c)
          if (codes.length == 0) {
            q.addOne("w.std.code like :code")
            params.addOne("%" + code.get + "%")
          } else {
            q.addOne("w.std.code in (:codes)")
            params.addOne(codes)
          }
        }
      }
      if (name.nonEmpty && !name.get.isBlank) {
        q.addOne("w.std.name like :name")
        params.addOne("%" + name.get + "%")
      }
      if (departId.nonEmpty) {
        q.addOne("w.std.state.department.id =:departId")
        params.addOne(departId)
      }
      if (q.nonEmpty) {
        val condition = new Condition(s"exists(from paper.writers as w where ${q.mkString(" and ")})")
        condition.params(params)
        query.where(condition)
      }
    }
    query
  }

}
