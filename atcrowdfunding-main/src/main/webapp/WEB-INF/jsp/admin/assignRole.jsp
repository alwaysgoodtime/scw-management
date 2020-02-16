<%--
  Created by IntelliJ IDEA.
  User: goodtime
  Date: 2020/2/16
  Time: 4:53 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="UTF-8">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <%@include file="/WEB-INF/jsp/common/script.jsp" %>
    <%@include file="/WEB-INF/jsp/common/css.jsp" %>
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
    </style>
</head>

<body>

    <jsp:include page="/WEB-INF/jsp/common/top.jsp"></jsp:include>
    <div class="container-fluid">
        <div class="row">
            <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp"></jsp:include>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <ol class="breadcrumb">
                    <li><a href="#">首页</a></li>
                    <li><a href="#">数据列表</a></li>
                    <li class="active">分配角色</li>
                </ol>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form role="form" class="form-inline">
                            <div class="form-group">
                                <label for="unAssigned">未分配角色列表</label><br>
                                <select class="form-control"  id="unAssigned" multiple size="10" style="width:250px;overflow-y:auto;">
                                    <c:forEach items="${unassigned}" var="role">
<%--                                        对于其中的遍历对象，一定记得加${},而且写在引号里--%>
                                        <option value="${role.id}">${role.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <ul>
                                    <li id="leftToRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                    <br>
                                    <li id="rightToLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                                </ul>
                            </div>
                            <div class="form-group" style="margin-left:40px;">
                                <label for="assign">已分配角色列表</label><br>
                                <select id="assign" class="form-control" multiple size="10" style="width:250px;overflow-y:auto;">
                                    <c:forEach items="${assigned}" var="roles">
                                        <%--对于其中的遍历对象，一定记得加${},而且写在引号里--%>
                                        <option value="${roles.id}">${roles.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $(".list-group-item").click(function(){
                if ( $(this).find("ul") ) {
                    $(this).toggleClass("tree-closed");
                    if ( $(this).hasClass("tree-closed") ) {
                        $("ul", this).hide("fast");
                    } else {
                        $("ul", this).show("fast");
                    }
                }
            });
        });

        //分配角色，前台页面显示+后台数据修改
        $("#leftToRightBtn").click(function () {
            var leftSelect = $("#unAssigned option:selected");//这个值就是一个集合
            if(leftSelect.length == 0){
                layer.msg("您没有选中任何选项",{icon:5,time:200});//简单的表单检查
            }else {
                var str = '';
                $.each(leftSelect,function (i,e) {//这里的function是回调函数，I是索引，E是索引对应的内容DOM元素
                    str += 'roleId='+e.value+"&";
                })
                str += "adminId=${param.id}";

                $.post("${PATH}/admin/doAssign",str,function (result) {
                    if("ok" == result){
                        layer.msg("分配成功",{icon:6,time:200},function () {
                            $("#assign").append(leftSelect.clone());
                            leftSelect.remove();//这里要传过去克隆的，再删除，如果不传克隆元素，也是追加不过去的,相当于两个都用了同一个元素，结果两边都没了
                        });
                    }else{
                        layer.msg("分配失败",{icon:6,time:200});
                    }
                })
            }


        })

        //取消分配角色，前台页面显示+后台数据修改
        $("#rightToLeftBtn").click(function () {
            var rightSelect = $("#assign option:selected");
            if(rightSelect.length == 0){
                layer.msg("您没有选中任何选项",{icon:5,time:200});//简单的表单检查
            }else {
                var str = '';
                $.each(rightSelect,function (i,e) {//这里的function是回调函数，I是索引，E是索引对应的内容DOM元素
                    str += 'roleId='+e.value+"&";
                })
                str += "adminId=${param.id}";

                $.post("${PATH}/admin/undoAssign",str,function (result) {
                    if("ok" == result){
                        layer.msg("分配成功",{icon:6,time:200},function () {
                            $("#unAssigned").append(rightSelect.clone());
                            rightSelect.remove();
                        });
                    }else{
                        layer.msg("分配失败",{icon:6,time:200});
                    }
                })
            }

        })

    </script>
</body>
</html>
