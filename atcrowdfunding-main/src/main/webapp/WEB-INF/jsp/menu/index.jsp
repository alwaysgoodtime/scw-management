    <%--
      Created by IntelliJ IDEA.
      User: goodtime
      Date: 2020/2/3
      Time: 7:36 下午
      To change this template use File | Settings | File Templates.
    --%>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="zh_CN">
        <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <%@include file="/WEB-INF/jsp/common/script.jsp" %>
        <%@include file="/WEB-INF/jsp/common/css.jsp" %>
        <link rel="stylesheet" href="${PATH}/static/css/doc.min.css">
        <style>
        .tree li {
        list-style-type: none;
        cursor: pointer;
        }

        table tbody tr:nth-child(odd) {
        background: #F4F4F4;
        }

        table tbody td:nth-child(even) {
        color: #C00;
        }
        </style>
        </head>

        <body>
        <jsp:include page="/WEB-INF/jsp/common/top.jsp"></jsp:include>
        <div class="container-fluid">
        <div class="row">
        <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp"></jsp:include>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <div class="panel panel-default">
        <div class="panel-heading">
        <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i>菜单列表</h3>
        </div>
        <div class="panel-body">
        <ul id="tree" class="ztree"></ul>
        </div> </div>
        </div>
        </div>
        </div>

        <%--添加模态框--%>
        <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
        aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title" id="myModalLabel">添加菜单</h4>
        </div>
        <div class="modal-body">
        <form role="form">
        <div class="form-group">
        <input type="hidden" value="" id="addPid">
        <%--      隐藏域的pid      --%>
        <label for="newName">名字</label>
        <input type="text" value="" class="form-control" id="newName" >
        <label for="newIcon">icon</label>
        <input type="text" value="" class="form-control" currentpage="" id="newIcon" >
        <label for="newUrl">链接</label>
        <input type="text" value="" class="form-control" id="newUrl" >
        </div>
        </form>
        </div>
        <div class="modal-footer">
        <button name="saveButton" type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" id="saveButton" class="btn btn-primary">保存</button>
        </div>
        </div>
        </div>
        </div>

        <%--更新模态框--%>
        <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
        aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title" id="ModalLabel">修改菜单</h4>
        </div>
        <div class="modal-body">
        <form role="form">
        <div class="form-group">
        <input type="hidden" value="" id="id">
        <input type="hidden" value="" id="pid">
        <label for="name">修改名字</label>
        <input type="text" value="" class="form-control" id="name" >
        <label for="icon">修改icon</label>
        <input type="text" value="" class="form-control" currentpage="" id="icon" >
        <label for="url">修改链接</label>
        <input type="text" value="" class="form-control" id="url" >
        </div>
        </form>
        </div>
        <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" id="updateButton" class="btn btn-primary">修改</button>
        </div>
        </div>
        </div>
        </div>

        <script type="text/javascript">
        $(function () {
        $(".list-group-item").click(function () {
        if ($(this).find("ul")) {
        $(this).toggleClass("tree-closed");
        if ($(this).hasClass("tree-closed")) {
        $("ul", this).hide("fast");
        } else {
        $("ul", this).show("fast");
        }
        }
        });

        loadTree();
        });

        function loadTree() {
        var setting = {
        data: {
        simpleData: {
        enable: true,
        pIdKey: "pid" //指定哪个为父ID，默认查找名称为pId属性
        },
        key:{
        url:"fuck"//指定一个不被识别的属性，这样点击菜单就不会执行url跳转了,否则就算设置为javascript:void(0)也会跳转
        }
        },
        view:{
        //z-tree的自定义属性
        addDiyDom: function(treeId,treeNode) {//改变字体前的图标
        $("#"+treeNode.tId+"_ico").removeClass();
        $("#"+treeNode.tId+"_span").before("<span class='"+treeNode.icon+"'></span>")
        },
        addHoverDom: function(treeId, treeNode){ //tree是容器，相当于ul，
        var aObj = $("#" + treeNode.tId + "_a"); // tId = permissionTree_1, ==> $("#permissionTree_1_a")
        //aObj.attr("href", "javascript:void(0);");//这么写不管用，得改key
        if (treeNode.editNameFlag || $("#btnGroup"+treeNode.id).length>0) return;//如果已经拼过串，就不用再拼了
        var s = '<span id="btnGroup'+treeNode.id+'">';
        if ( treeNode.level == 0 ) {//节点深度为0，超级根节点，有增加子节点按钮
        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" onclick="addBtn('+treeNode.id+')" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
        }
        else if ( treeNode.level == 1 ) {//节点深度为1，有修改权限按钮
        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" menuPid="'+treeNode.pid+'" menuName="'+treeNode.name+'" menuUrl="'+treeNode.url+'" menuIcon="'+treeNode.icon+'" onclick="updateBtn('+treeNode.id+',this)" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';

        if (treeNode.childMenu.length == 0) {//深度为1的节点没孩子，多了删除按钮，这里找到都是TMenu自身的属性，所以要写childMenu
        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" menuPid="'+treeNode.pid+'" menuName="'+treeNode.name+'" menuUrl="'+treeNode.url+'" menuIcon="'+treeNode.icon+'" onclick="deleteBtn('+treeNode.id+')">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
        }
        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" onclick="addBtn('+treeNode.id+')">&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
        }

        else if ( treeNode.level == 2 ) {//节点深度为2，最小的子节点，有修改权限和删除按钮
        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  menuPid="'+treeNode.pid+'" menuName="'+treeNode.name+'" menuUrl="'+treeNode.url+'" menuIcon="'+treeNode.icon+'" onclick="updateBtn('+treeNode.id+',this)" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" menuPid="'+treeNode.pid+'" menuName="'+treeNode.name+'" menuUrl="'+treeNode.url+'" menuIcon="'+treeNode.icon+'" onclick="deleteBtn('+treeNode.id+')">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
        }
        s += '</span>';
        aObj.after(s);
        },
        removeHoverDom: function(treeId, treeNode){
        $("#btnGroup"+treeNode.id).remove();
        }
        }
        };
        $.get("${PATH}/menu/loadData",function(result) {
        var treenode = result;
        treenode.push({id:0,name:"系统菜单",icon:"glyphicon glyphicon-align-justify"});//在返回的json数据中加入父节点
        $.fn.zTree.init($("#tree"), setting,result);
        var ztreeObj = $.fn.zTree.getZTreeObj("tree"); //展开节点，这里写父元素的id
        ztreeObj.expandAll(true);
        });
        };

        //删除按钮

        function deleteBtn(id){
            layer.confirm('您确定要删除吗？', {
            btn: ['确定', '返回']
            }, function () {
            $.post("${PATH}/menu/delete", {id: id}, function (result) {
            if ("ok" == result) {
            layer.msg('删除成功', {icon: 1}, {time: 50});
            window.location.href="${PATH}/menu/index";//这里不只是重新加载tree，而是将菜单栏重新加载，而且必须清空菜单的session，重新注入session
            } else {
            layer.msg('删除失败', {icon: 0}, {time: 50});
            }
            })
            }, function (index) {
            layer.close(index);
            });
        }

        //添加按钮
        function addBtn(pid){
        $("#addModal").modal({
        show:true,
        backdrop:'static',
        keyboard:false
        })
        $("#addModal input[id='addPid']").val(pid);//通过jquery的val函数挂上去
        }
        //添加按钮的确认按钮
        $("#saveButton").click(function() {
        var pid = $("#addModal input[id='addPid']").val();
        var name = $("#addModal input[id='newName']").val();
        var icon = $("#addModal input[id='newIcon']").val();
        var url = $("#addModal input[id='newUrl']").val();
        var value = $("#addRole").val();
        $.post("${PATH}/menu/add", {pid:pid,name:name,icon:icon,url:url}, function (result) {
            $("#addModal input[id='addPid']").val("");
            $("#addModal input[id='newName']").val("");
            $("#addModal input[id='newIcon']").val("");
            $("#addModal input[id='newUrl']").val("");//添加模态框没有回显，这样可以清空上次输入到添加模态框的值
        if ("ok" == result) {
        layer.msg("添加成功", {icon: 1, time: 500}, function () {
        $("#addModal").modal('hide');//成功记得关模态框
        });
        window.location.href="${PATH}/menu/index";//这里不只是重新加载tree，而是将菜单栏重新加载，而且必须清空菜单的session，重新注入session
        } else if ("403" == result) {
        layer.msg("您的访问权限不够", {icon: 0, time: 1000});
        } else {
        layer.msg("添加失败", {icon: 0, time: 500});
        }
        })
        })

        //修改按钮
        function updateBtn(id,btn){//取回原来的那个按钮this
            $("#updateModal").modal({
                backdrop: 'static',
                show:true,
                keyboard: false
            })
            let pid = $(btn).attr("menuPid");
            let url= $(btn).attr("menuUrl");
            let name = $(btn).attr("menuName");
            let icon = $(btn).attr("menuIcon");
            $("#pid").val(pid);
            $("#url").val(url);
            $("#name").val(name);
            $("#icon").val(icon);
            $("#id").val(id);
        }

        $("#updateButton").click(function() {

            let pid = $("#pid").val();
            let url = $("#url").val();
            let name = $("#name").val();
            let icon = $("#icon").val();
            let id = $("#id").val();

            $.ajax({
                url:"${PATH}/menu/update",    //请求的url地址
                async:true,//请求是否异步，默认为异步，这也是ajax重要特性
                data:{"id":id,pid:pid,url:url,name:name,icon:icon},    //参数值
                type:"POST",   //请求方式
                beforeSend:function(){
                    //请求前的处理
                index = layer.load(0, {time: 10 * 1000});//不给index加前缀，它就是全局变量
                return true;
                },
                success:function(result){
                    //请求成功时处理
                    if("ok" == result){
                    layer.close(index);
                    $("#updateModal").modal('hide');
                    window.location.href="${PATH}/menu/index";//这里不只是重新加载tree，而是将菜单栏重新加载，而且必须清空菜单的session，重新注入session
                    }
                    else{
                       layer.close(aa);
                       $("#updateModal").modal('hide');
                       layer.msg("修改失败", {icon: 0, time: 500});
                     }
                }
            });
            })

        </script>
        </body>
        </html>