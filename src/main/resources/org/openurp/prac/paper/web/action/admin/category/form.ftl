[#ftl]
[@b.head/]
[@b.toolbar title="修改论文主题"]bar.addBack();[/@]
  [@b.form action=b.rest.save(category) theme="list"]
    [@b.textfield name="category.code" label="代码" value="${category.code!}" required="true" maxlength="20"/]
    [@b.textfield name="category.name" label="名称" value="${category.name!}" required="true" maxlength="20"/]
    [@b.textfield name="category.enName" label="英文名称" value="${category.enName!}" maxlength="100"/]
    [@b.startend label="有效期限"
      name="category.beginOn,category.endOn" required="true,false"
      start=category.beginOn end=category.endOn format="date"/]
    [@b.textfield name="category.remark" label="备注" value="${category.remark!}" maxlength="3"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[@b.foot/]
