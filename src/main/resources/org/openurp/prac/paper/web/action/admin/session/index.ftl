[#ftl]
[@b.head/]
[#include "../setting.ftl"/]
<div class="search-container">
    <div class="search-panel" >
    [@b.form name="batchSearchForm" action="!search" target="batchlist" title="ui.searchForm" theme="search"]
      [@base.semester name="session.semester.id" value=semester label="学年学期"/]
      [@b.textfields names="batch.name;名称"/]
      <input type="hidden" name="orderBy" value="name desc"/>
    [/@]
    </div>
    <div class="search-list">
    [@b.div id="batchlist" href="!search?orderBy=name desc"/]
    </div>
  </div>
[@b.foot/]
