[#ftl]
[@b.head/]
[#include "../setting.ftl"/]
<div class="search-container">
    <div class="search-panel">
    [@b.form name="categorySearchForm" action="!search" target="categorylist" title="ui.searchForm" theme="search"]
      [@b.textfields names="category.code;代码"/]
      [@b.textfields names="category.name;名称"/]
      <input type="hidden" name="orderBy" value="category.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="categorylist" href="!search?orderBy=category.code"/]
  </div>
</div>
[@b.foot/]
