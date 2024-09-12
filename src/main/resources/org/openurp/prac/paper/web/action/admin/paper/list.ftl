[@b.head/]
[#macro display longName width]
<div style="overflow: hidden;text-overflow: ellipsis;width: ${width}px;display: inline-block;white-space: nowrap;" title="${longName}">${longName}</div>
[/#macro]
[#assign extMap={"xls":'xls.gif',"xlsx":'xls.gif',"docx":"doc.gif","doc":"doc.gif","pdf":"pdf.gif","zip":"zip.gif","":"generic.gif"}]

[@b.grid items=papers var="paper"]
  [@b.gridbar]
    bar.addItem("${b.text("action.export")}",action.exportData("title:题目,subjectName:主题,"+
                "writerCodes:学号,writerNames:姓名,department.name:学院,majorNames:专业,squadNames:班级,category.name:类型,"+
                "advisor.name:指导教师,mobile:联系电话,submitAt:提交时间",null,'fileName=竞赛申请信息'));
    bar.addItem("下载论文",action.multi("download",null,null,false),"action-download");
  [/@]
  [@b.row]
    [@b.boxcol/]
    [@b.col property="title" title="题目"]
      [#if paper.filePath?starts_with("/")]
        [@b.a href="!download?paper.id="+paper.id target="_blank"]<div class="text-ellipsis" title="${(paper.title)!}">${(paper.title)!}</div>[/@]
      [#else]
        ${paper.title}
      [/#if]
    [/@]
    [@b.col title="主题" width="100px" property="subject.name" /]
    [@b.col title="学号" width="120px"]${paper.writerCodes}[/@]
    [@b.col title="姓名" width="100px"]${paper.writerNames}[/@]
    [@b.col title="学院" width="120px"]${(paper.department.name)!}[/@]
    [@b.col title="类型" width="100px" property="category.name"/]
    [@b.col title="指导教师" width="100px" property="advisor.name"]${(paper.advisor.name)!'无'}[/@]
    [@b.col title="上传时间" property="submitAt" width="120px"]
      ${paper.submitAt?string('MM-dd HH:mm')}
    [/@]
    [@b.col title="格式" property="fileExt" width="50px"]
      [#if paper.filePath?starts_with("/")]
      <image src="${b.static_url("ems","images/file/"+extMap[paper.filePath?keep_after_last(".")]?default("generic.gif"))}"/>
      [#else]
       无
      [/#if]
    [/@]
  [/@]
[/@]
[@b.foot/]
