[#ftl]
[@b.head/]
[@b.grid items=sessions var="session"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="35%" property="name" title="名称"/]
    [@b.col width="30%" property="beginAt" title="开始时间"]
      ${session.beginAt?string('yyyy-MM-dd HH:mm')}
    [/@]
    [@b.col width="30%" property="endAt" title="结束时间"]
      ${session.endAt?string('yyyy-MM-dd HH:mm')}
    [/@]
  [/@]
[/@]
[@b.foot/]
