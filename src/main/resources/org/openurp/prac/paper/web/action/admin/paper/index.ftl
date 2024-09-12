[@b.head/]
[@b.toolbar title='本科学术论文竞赛']
[/@]
<div class="search-container">
    <div class="search-panel">
      [@b.form name="paperSearchForm" action="!search" title="ui.searchForm" target="paperList" theme="search"]
        [@base.semester name="paper.semester.id" label="学年学期" value=semester required="true"/]
        [@b.textfield name="writer.std.code" label="学号" maxlength="20000"/]
        [@b.textfield name="writer.std.name" label="姓名"/]
        [@b.select name="writer.std.state.department.id" label="院系" items=departs/]
        [@b.select name="paper.filePath" label="格式" items={'doc':'.doc','docx':'.docx','pdf':'.pdf','wps':'.wps'} empty="..."/]
      [/@]
      <script>
        $(document).ready(function() {
          bg.form.submit("paperSearchForm");
        });
      </script>
    </div>
    <div class="search-list">[@b.div id="paperList"/]</div>
</div>
[@b.foot/]
