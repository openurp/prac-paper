[#ftl]
[@b.head/]
[@b.toolbar title="学期竞赛设置"]bar.addBack();[/@]
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(session)]
    [@b.textfield name="session.name" label="名称" value=session.name! required="true" maxlength="80"/]
    [@b.field label="学年学期"]${session.semester.schoolYear}学年度${session.semester.name}学期[/@]
    [@b.startend label="起始结束"
      name="session.beginAt,session.endAt" required="true"
      start=(session.beginAt)! end=(session.endAt)! format="datetime" style="width:150px"/]
    [@b.textarea label="简短通知" rows="5" cols="80" name="session.notice" required="true" value=session.notice!  maxlength="1000"/]
    [@b.formfoot]
      <input type="hidden" name="session.semester.id" value="${session.semester.id}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]
