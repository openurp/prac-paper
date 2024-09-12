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

import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.{Project, Semester}
import org.openurp.prac.paper.model.PaperSession
import org.openurp.starter.web.support.ProjectSupport

class SessionAction extends RestfulAction[PaperSession], ProjectSupport {

  override protected def simpleEntityName: String = "session"

  override protected def indexSetting(): Unit = {
    given project: Project = getProject

    put("project", project)
    put("semester", getSemester)
  }

  override protected def editSetting(session: PaperSession): Unit = {
    given project: Project = getProject

    put("project", project)
    if (null == session.semester) session.semester = getSemester
    else session.semester = entityDao.get(classOf[Semester], session.semester.id)
    super.editSetting(session)
  }

  override protected def saveAndRedirect(session: PaperSession): View = {
    session.project = getProject
    super.saveAndRedirect(session)
  }
}
