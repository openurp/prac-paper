[#ftl]
[@b.head/]
[@b.toolbar title="学术论文竞赛作品提交"]bar.addBack();[/@]
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(paper)]
    [@b.field label="学年学期"]${paper.semester.schoolYear}学年度${paper.semester.name}学期[/@]

    [@b.textfield name="paper.title" label="作品名称" value=paper.title! required="true" maxlength="100"/]
    [@b.select name="paper.category.id" label="作品类型" value=paper.category! items=categories required="true" onchange="displayWriters(this.value)"/]
    [@b.select name="paper.subject.id" label="作品主题" value=paper.subject! items=subjects empty="..." required="true"/]
    [@b.textfield name="paper.subSubjectName" label="其他主题名称" value=paper.subSubjectName! maxlength="100" comment="当主题选择其他时，请填写"/]
    [@base.student label="作者1" name="writer1.std.id" value=paper.getWriter(1?int) required="true" empty="..."/]
    [@base.student label="作者2" name="writer2.std.id" value=paper.getWriter(2?int) required="false" empty="..."/]
    [@base.student label="作者3" name="writer3.std.id" class="over3writer" value=paper.getWriter(3?int) required="false" empty="..." style="width:200px"/]
    [@base.student label="作者4" name="writer4.std.id" class="over3writer" value=paper.getWriter(4?int) required="false" empty="..." style="width:200px"/]
    [@base.student label="作者5" name="writer5.std.id" class="over3writer" value=paper.getWriter(5?int) required="false" empty="..." style="width:200px"/]
    [@base.teacher label="指导教师" name="paper.advisor.id" value=paper.advisor! required="false" empty="请选择指导教师" comment="确定无指导老师，可以不选"]
    [/@]
    [@b.cellphone name="paper.mobile" required="true" label="联系手机" value=paper.mobile! /]

    [#if paper.filePath??]
      [@b.field label="已上传论文"]
         <span class="text-muted">${paper.submitAt?string("yyyy-MM-dd HH:mm")}</span>
         [@b.a href="!download?paper.id="+paper.id target="_blank"]<i class="fa-solid fa-download"></i>下载[/@]
      [/@]
      [@b.file name="file" extensions="pdf" maxSize="30M" label="更新论文"/]
    [#else]
      [@b.file name="file" extensions="pdf" maxSize="30M"  required="true" label="选择文件"/]
    [/#if]
    [@b.formfoot]
      <input name="projectId" type="hidden" value="${std.project.id}"/>
      <input name="paper.semester.id" type="hidden" value="${paper.semester.id}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
<script>
  function displayWriters(categoryId){
    var elem3 = jQuery("select[name='writer3\.std\.id']");
    var elem4 = jQuery("select[name='writer4\.std\.id']");
    var elem5 = jQuery("select[name='writer5\.std\.id']");
    var elems = [elem3,elem4,elem5];
    for(var i=0;i < elems.length;i++){
      var elem = elems[i];
      if(categoryId=="1"){
        elem.parent().hide();
      }else{
        elem.parent().show();
      }
    }
  }
  [#if paper.category??]
    displayWriters("${paper.category.id}");
  [/#if]
</script>
[@b.foot/]
