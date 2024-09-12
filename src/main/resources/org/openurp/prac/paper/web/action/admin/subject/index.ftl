[#ftl]
[@b.head/]
[#include "../setting.ftl"/]
<div class="search-container">
    <div class="search-panel">
    [@b.form name="subjectSearchForm" action="!search" target="subjectlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="subject.code;代码"/]
      [@b.textfields names="subject.name;名称"/]
      <input type="hidden" name="orderBy" value="subject.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="subjectlist" href="!search?orderBy=subject.code"/]
  </div>
</div>
[@b.foot/]
