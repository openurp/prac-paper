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

import org.beangle.data.orm.MappingModule

class DefaultMapping extends MappingModule {

  def binding(): Unit = {
    bind[Paper] declare { e =>
      e.writers is depends("paper")
      e.title is length(200)
      e.mobile is length(15)
      e.fileExt is length(100)
      e.filePath is length(200)
    }
    bind[PaperSession] declare { e =>
      e.notice is length(2000)
    }
    bind[PaperSubject] declare { e =>
      e.name is length(50)
    }
    bind[PaperCategory] declare { e =>
      e.name is length(50)
    }

    bind[PaperWriter]
  }
}
