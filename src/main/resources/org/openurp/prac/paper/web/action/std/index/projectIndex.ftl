[#ftl]
[@b.head/]
[@b.toolbar title="本科生学术论文竞赛，作品提交"]bar.addBack();[/@]

<div class="container">

  [@b.messages slash="3"/]
  <div class="jumbotron">
  [#if session??]
    <h2>${session.name}开始了<span style="font-size:0.5em">(${session.beginAt?string("MM-dd")}~${session.endAt?string("MM-dd")})</span></h2>
    <p>${session.notice!}</p>
    <p>[@b.a class="btn btn-primary btn-lg" href="!applyForm?projectId="+project.id+"&semester.id="+session.semester.id role="button"]提交论文[/@]</p>
  [#else]
    <h2>学术论文竞赛报名尚未开始.</h2>
  [/#if]
  </div>

  [#if papers?size>0]
  <div class="card card-info card-primary card-outline">
    <div class="card-header border-transparent">
        <h3 class="card-title"><i class="far fa-bell"></i>所有报名信息</h3>
    </div>
    <div class="card-body p-0">
      <div class="table-responsive">
        <table class="table no-margin m-0 compact">
          <thead>
            <tr>
              <th>标题</th><th>类型</th><th>作者</th><th>主题</th><th>指导教师</th><th>操作</th><th>提交时间</th>
            </tr>
          </thead>
          <tbody>
          [#assign extMap={"xls":'xls.gif',"xlsx":'xls.gif',"docx":"doc.gif","doc":"doc.gif","pdf":"pdf.gif","zip":"zip.gif","":"generic.gif"}]
          [#list papers as paper]
            <tr>
              <td>
                [@b.a href="!download?paper.id="+paper.id  target="_blank"]
                <image src="${b.static_url("ems","images/file/"+extMap[paper.filePath?keep_after_last(".")]?default("generic.gif"))}"/>&nbsp;${paper.title}
                [/@]
              </td>
              <td>${(paper.category.name)!}</td>
              <td>${paper.writerNames}</td>
              <td>${paper.subjectName}</td>
              <td>${(paper.advisor.name)!"无"}</td>
              <td style="width:220px">
              [@b.a href="!download?paper.id="+paper.id role="button" class="btn btn-success btn-sm" target="_blank"]<i class="fa-solid fa-download"></i>下载[/@]
              [#if session?? && session.semester=paper.semester]
              [@b.a href="!applyForm?projectId="+project.id+"&semester.id="+paper.semester.id role="button" class="btn btn-primary btn-sm"]<i class="fa-solid fa-pen-to-square"></i>修改[/@]
              [@b.a href="!remove?paper.id="+paper.id role="button" class="btn btn-danger btn-sm" onclick="return bg.Go(this,null,'确定撤销作品?')"]<i class="fa-solid fa-xmark"></i>撤销[/@]
              [/#if]
              </td>
              <td style="width:180px"><span class="text-muted">${paper.submitAt?string('yyyy-MM-dd HH:mm')}</span></td>
            </tr>
          [/#list]
          </tbody>
        </table>
      </div>
    </div>
  </div>
  [/#if]

</div>

[@b.foot/]
