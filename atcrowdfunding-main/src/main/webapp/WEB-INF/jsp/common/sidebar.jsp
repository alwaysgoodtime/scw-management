<%--
  Created by IntelliJ IDEA.
  User: goodtime
  Date: 2020/2/2
  Time: 5:49 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-sm-3 col-md-2 sidebar">
    <div class="tree">
        <ul style="padding-left:0px;" class="list-group">
            <c:forEach items="${menuList}" var="menu" >
                <c:if test="${empty menu.childMenu}">
                <li class="list-group-item tree-closed">
                    <a href="${PATH}${menu.url}"><i class="${menu.icon}"></i>${menu.name}</a>
                </li>
                </c:if>
                <c:if test="${not empty menu.childMenu}">
                    <li class="list-group-item tree-closed">
                        <span><i class="${menu.icon}"></i>${menu.name}<span class="badge" style="float:right">${menu.childMenu.size()}</span></span>
                        <ul style="margin-top:10px;display:none;">
                            <c:forEach items="${menu.childMenu}" var="child">
                                <li style="height:30px;">
                                    <a href="${PATH}${child.url}"><i class="${child.icon}"></i>${child.name}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</div>