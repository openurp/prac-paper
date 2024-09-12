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

package org.openurp.prac.paper.model

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.openurp.base.hr.model.Teacher
import org.openurp.base.model.{Department, Semester}
import org.openurp.base.std.model.Student

import java.time.Instant
import scala.collection.mutable

/** 学术论文
 */
class Paper extends LongId {
  /** 类型 */
  var category: PaperCategory = _
  /** 主题 */
  var subject: PaperSubject = _
  /** 主题说明 */
  var subSubjectName: Option[String] = None
  /** 学生 */
  var writers: mutable.Buffer[PaperWriter] = Collections.newBuffer[PaperWriter]
  /** 论文题目 */
  var title: String = _
  /** 学年学期 */
  var semester: Semester = _
  /** 附件路径 */
  var filePath: String = _
  /** 附件类型 */
  var fileExt: String = _
  /** 联系电话 */
  var mobile: String = _
  /** 提交时间 */
  var submitAt: Instant = _
  /** 指导老师 */
  var advisor: Option[Teacher] = None

  def subjectName: String = {
    subSubjectName match
      case None => subject.name
      case Some(s) => subject.name + s"(${s})"
  }

  def department: Department = {
    if writers.isEmpty then null.asInstanceOf[Department]
    else writers.head.std.department
  }

  def majorNames: String = {
    writers.map(_.std.major.name).distinct.mkString(",")
  }

  def squadNames: String = {
    writers.map(_.std.state.get.squad.map(_.name).getOrElse("")).distinct.mkString(",")
  }

  def isWriter(std: Student): Boolean = {
    writers.exists(x => x.std == std)
  }

  def addWriter(idx: Int, std: Student): PaperWriter = {
    writers.find(x => x.std == std) match
      case None =>
        writers.find(x => x.idx == idx) match
          case None =>
            val pw = new PaperWriter
            pw.paper = this
            pw.std = std
            pw.idx = idx
            writers.addOne(pw)
            pw
          case Some(pw) =>
            pw.std = std
            pw
      case Some(pw) =>
        pw.idx = idx
        pw
  }

  def writerCodes: String = {
    writers.map(_.std.code).mkString(" ")
  }

  def writerNames: String = {
    writers.map(_.std.name).mkString(" ")
  }

  def getWriter(idx: Int): Option[Student] = {
    writers.find(x => x.idx == idx).map(_.std)
  }
}
