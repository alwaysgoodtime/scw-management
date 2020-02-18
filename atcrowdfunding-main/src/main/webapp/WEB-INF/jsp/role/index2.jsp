<%--
  Created by IntelliJ IDEA.
  User: goodtime
  Date: 2020/2/11
  Time: 10:52 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
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
                        <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                    </div>
                    <div class="panel-body">
                        <form class="form-inline" role="form" style="float:left;">
                            <div class="form-group has-feedback">
                                <div class="input-group">
                                    <div class="input-group-addon">查询条件</div>
                                    <input class="form-control has-success" id="searchName" type="text"
                                           placeholder="请输入查询条件">
                                </div>
                            </div>
                            <button type="button" onclick="search()" class="btn btn-warning"><i
                                    class="glyphicon glyphicon-search"></i> 查询
                            </button>
                        </form>
                        <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i
                                class=" glyphicon glyphicon-remove"></i> 删除
                        </button>
                        <%--有对应权限的人才能看到--%>
                        <security:authorize access="hasRole('PM - 项目经理')">
                            <button type="button" class="btn btn-primary" style="float:right;"
                                    id="addBtn"><i class="glyphicon glyphicon-plus"></i> 新增
                            </button>
                        </security:authorize>

                        <br>
                        <hr style="clear:both;">
                        <div class="table-responsive">
                            <table class="table  table-bordered">
                                <thead>
                                    <tr>
                                        <th width="30">#</th>
                                        <th width="30"><input type="checkbox"></th>
                                        <th>名称</th>
                                        <th width="100">操作</th>
                                    </tr>
                                </thead>
                                <tbody id="table">
                                </tbody>
                                <tfoot>
                                <tr>
                                    <td colspan="6" align="center">
                                        <ul class="pagination" id="nav">
                                        </ul>
                                    </td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
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
                    <h4 class="modal-title" id="myModalLabel">添加角色</h4>
                </div>
                <div class="modal-body">
                    <form role="form">
                        <div class="form-group">
                            <label for="addRole">添加角色</label>
                            <input type="text" class="form-control" id="addRole" placeholder="请添加新角色">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
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
                    <h4 class="modal-title" id="ModalLabel">修改角色</h4>
                </div>
                <div class="modal-body">
                    <form role="form">
                        <div class="form-group">
                            <label for="updateMsg">修改角色</label>
                            <input type="hidden" value="" id="roleId">
                            <input type="text" value="" class="form-control" currentpage="" id="updateMsg">
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


    <form role="form">

    </form>


    <script type="text/javascript">
        $(function () {//页面加载完需要执行的事件处理
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

            initdata(1);//页面加载完执行ajax请求，取回数据
        });
        var json = {
            currentPage: 1,
            pageSize: 3,
            condition: ""
        }

        //通过索引关弹窗
        var index = -1;

        //异步初始化，发送ajax请求获得值
        function initdata(pageNum) {
            json.currentPage = pageNum;
            $.ajax({
                url: "${PATH}/role/loadData",//注意，结尾是逗号，不是分号
                type: "get",
                data: json,
                beforeSend: function () {
                    //没加载完就会转10秒
                    index = layer.load(0, {time: 10 * 1000});
                    return true;
                },
                success: function (result) {
                    layer.close(index);//关闭ajax异步加载等待窗口
                    //拼串，局部加载
                    initShow(result);
                    initNav(result);
                }
            });
        }

        //异步查询
        function search() {
            let value = $("#searchName").val();//记住，val()是取元素中的值
            console.log(value);
            json.condition = value;
            initdata(1);//调用ajax查询
        }

        //处理异步得来的值，展示数据
        function initShow(result) {
            $("#table").empty();//因为异步请求要不断往进塞数据，每次调用记得清空
            var list = result.list;
            $.each(list, function (i, e) {//i为索引index，e为element
                //开始拼串,也可以用append一直append，不过拼串真香
                var a = '';//一定要是''，不能是""
                a += '<td>' + (i + 1) + '</td>';
                a += '<td><input type="checkbox"></td>';
                a += '<td>' + e.name + '</td>';
                a += '<td>' +
                    '<button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>' +
                    '<button type="button" currentpage="' + result.pageNum + '" rolename="' + e.name + '"update="' + e.id + '" class="updateBtn btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>' +
                    '<button type="button" currentpage="' + result.pageNum + '" delete="' + e.id + '" class="btn deleteBtn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>' +
                    '</td>';
                $("#table").append('<tr>' + a + '</tr>');
            });
        }

        //处理异步得到的值，展示分页栏
        function initNav(result) {
            $("#nav").empty();//因为异步请求要不断往进塞数据，每次调用记得清空
            var page = result.navigatepageNums;
            var a = '';
            if (result.pageNum == 1 || result.pageNum == 0) {
                a += '<li class="disabled"><a href="#">上一页</a></li>';
            } else {
                a += '<li class="active"><a onclick="initdata(' + (result.pageNum - 1) + ')">上一页</a></li>';//数字记得加括号
                //  异步ajax，不能发href请求，继续调用ajax
            }

            $.each(page, function (i, num) {
                if (result.pageNum != num) {
                    a += '<li><a onclick="initdata(' + num + ')">' + num + '</a></li>';
                } else {
                    a += '<li class="active"><a onclick="initdata(' + num + ')">' + num + '<span class="sr-only">(current)</span></a></li>';
                }
            })

            if (result.isLastPage) {
                a += '<li class="disabled"><a href="#">下一页</a></li>';
            } else {
                a += '<li class="active"><a onclick="initdata(' + (result.pageNum + 1) + ')">下一页</a></li>';//数字记得加括号
                //  异步ajax，不能发href请求，继续调用ajax
            }
            $("#nav").append(a);
        }

        //删除角色按钮
        $("#table").on('click', '.deleteBtn', function () {//直接是找不到deleteBtn这个class的，因为我们是发送ajax动态生成的
            let id = $(this).attr("delete");//this就是那个按钮，自定义属性用attr，不用prop
            let currentpage = $(this).attr("currentpage");//当前页，删除后跳到当前页，如果是最后一页删除，跳到最后一页
            layer.confirm('您确定要删除吗？', {
                btn: ['确定', '返回']
            }, function () {
                $.post("${PATH}/role/delete", {id: id}, function (result) {
                    if ("ok" == result) {
                        layer.msg('删除成功', {icon: 1}, {time: 500});
                        initdata(currentpage);
                    } else {
                        layer.msg('删除失败', {icon: 0}, {time: 500});
                    }
                })
            }, function (index) {
                layer.close(index);
            });
        })


        //更新角色按钮
        $("#table").on('click', '.updateBtn', function () {
            let id = $(this).attr("update");//方便回写数据到模态框,必须把this转换成jquery对象，this对象不能直接取自定义属性
            let name = $(this).attr("rolename");//名字
            let currentpage = $(this).attr("currentpage");//当前页
            $("#roleId").attr("value", id);//角色id是隐藏的
            $("#updateMsg").attr("value", name);//回显角色名字
            $("#updateMsg").attr("currentpage", currentpage);//回显当前页数
            $("#updateModal").modal({
                show: true
            })
        });


        $("#updateButton").click(function () {
            let val = $("#roleId").val();
            let name = $("#updateMsg").val();
            console.log(name);
            $.post("${PATH}/role/update", {id: val, name: name}, function (result) {
                if ("ok" == result) {
                    layer.msg("修改成功", {icon: 1, time: 500}, function () {
                        $("#updateModal").modal('hide');//成功记得关模态框
                        initdata($("#updateMsg").currentPage);
                    });
                } else {
                    layer.msg("修改失败", {icon: 0, time: 500});
                }
            })
        })

        //添加角色按钮
        $("#addBtn").click(function () {
            $("#addModal").modal({
                show: true
            });
        });

        $("#saveButton").click(function () {
            var value = $("#addRole").val();
            $.post("${PATH}/role/add", {name: value}, function (result) {
                if ("ok" == result) {
                    layer.msg("添加成功", {icon: 1, time: 500}, function () {
                        $("#addModal").modal('hide');//成功记得关模态框
                        initdata(10000);//发起一个异步请求，添加一个非常大的值，因为pagehelper的合理化，会自动
                        //跳转到最后一页，也就能看到我们添加的角色了
                    });
                } else if ("403" == result) {
                    layer.msg("您的访问权限不够", {icon: 0, time: 1000});
                } else {
                    layer.msg("添加失败", {icon: 0, time: 500});
                }
            })
        })

    </script>
</body>
</html>
