/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2019-11-22 15:25:46 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class item_002dparam_002dadd_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<table cellpadding=\"5\" style=\"margin-left: 30px\" id=\"itemParamAddTable\" class=\"itemParam\">\r\n");
      out.write("    <tr>\r\n");
      out.write("        <td>商品类目:</td>\r\n");
      out.write("        <td><a href=\"javascript:void(0)\" class=\"easyui-linkbutton selectItemCat\">选择类目</a>\r\n");
      out.write("            <input type=\"hidden\" name=\"cid\" style=\"width: 280px;\"></input>\r\n");
      out.write("        </td>\r\n");
      out.write("    </tr>\r\n");
      out.write("    <tr class=\"hide addGroupTr\">\r\n");
      out.write("        <td>规格参数:</td>\r\n");
      out.write("        <td>\r\n");
      out.write("            <ul>\r\n");
      out.write("                <li><a href=\"javascript:void(0)\" class=\"easyui-linkbutton addGroup\">添加分组</a></li>\r\n");
      out.write("            </ul>\r\n");
      out.write("        </td>\r\n");
      out.write("    </tr>\r\n");
      out.write("    <tr>\r\n");
      out.write("        <td></td>\r\n");
      out.write("        <td>\r\n");
      out.write("            <a href=\"javascript:void(0)\" class=\"easyui-linkbutton submit\">提交</a>\r\n");
      out.write("            <a href=\"javascript:void(0)\" class=\"easyui-linkbutton close\">关闭</a>\r\n");
      out.write("        </td>\r\n");
      out.write("    </tr>\r\n");
      out.write("</table>\r\n");
      out.write("<div class=\"itemParamAddTemplate\" style=\"display: none;\">\r\n");
      out.write("    <li class=\"param\">\r\n");
      out.write("        <ul>\r\n");
      out.write("            <li>\r\n");
      out.write("                <input class=\"easyui-textbox\" style=\"width: 150px;\" name=\"group\"/>&nbsp;<a href=\"javascript:void(0)\"\r\n");
      out.write("                                                                                           class=\"easyui-linkbutton addParam\"\r\n");
      out.write("                                                                                           title=\"添加参数\"\r\n");
      out.write("                                                                                           data-options=\"plain:true,iconCls:'icon-add'\"></a>\r\n");
      out.write("            </li>\r\n");
      out.write("            <li>\r\n");
      out.write("                <span>|-------</span><input style=\"width: 150px;\" class=\"easyui-textbox\" name=\"param\"/>&nbsp;<a\r\n");
      out.write("                    href=\"javascript:void(0)\" class=\"easyui-linkbutton delParam\" title=\"删除\"\r\n");
      out.write("                    data-options=\"plain:true,iconCls:'icon-cancel'\"></a>\r\n");
      out.write("            </li>\r\n");
      out.write("        </ul>\r\n");
      out.write("    </li>\r\n");
      out.write("</div>\r\n");
      out.write("<script style=\"type:text/javascript\">\r\n");
      out.write("    $(function () {\r\n");
      out.write("        EGO.initItemCat({\r\n");
      out.write("            fun: function (node) {\r\n");
      out.write("                $(\".addGroupTr\").hide().find(\".param\").remove();\r\n");
      out.write("                //  判断选择的目录是否已经添加过规格\r\n");
      out.write("                $.getJSON(\"/item/param/query/itemcatid/\" + node.id, function (data) {\r\n");
      out.write("                    if (data.status == 200 && data.data) {\r\n");
      out.write("                        $.messager.alert(\"提示\", \"该类目已经添加，请选择其他类目。\", undefined, function () {\r\n");
      out.write("                            $(\"#itemParamAddTable .selectItemCat\").click();\r\n");
      out.write("                        });\r\n");
      out.write("                        return;\r\n");
      out.write("                    }\r\n");
      out.write("                    $(\".addGroupTr\").show();\r\n");
      out.write("                });\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("\r\n");
      out.write("        $(\".addGroup\").click(function () {\r\n");
      out.write("            var temple = $(\".itemParamAddTemplate li\").eq(0).clone();\r\n");
      out.write("            $(this).parent().parent().append(temple);\r\n");
      out.write("            temple.find(\".addParam\").click(function () {\r\n");
      out.write("                var li = $(\".itemParamAddTemplate li\").eq(2).clone();\r\n");
      out.write("                li.find(\".delParam\").click(function () {\r\n");
      out.write("                    $(this).parent().remove();\r\n");
      out.write("                });\r\n");
      out.write("                li.appendTo($(this).parentsUntil(\"ul\").parent());\r\n");
      out.write("            });\r\n");
      out.write("            temple.find(\".delParam\").click(function () {\r\n");
      out.write("                $(this).parent().remove();\r\n");
      out.write("            });\r\n");
      out.write("        });\r\n");
      out.write("\r\n");
      out.write("        $(\"#itemParamAddTable .close\").click(function () {\r\n");
      out.write("            $(\".panel-tool-close\").click();\r\n");
      out.write("        });\r\n");
      out.write("\r\n");
      out.write("        $(\"#itemParamAddTable .submit\").click(function () {\r\n");
      out.write("            var params = [];\r\n");
      out.write("            var groups = $(\"#itemParamAddTable [name=group]\");\r\n");
      out.write("            groups.each(function (i, e) {\r\n");
      out.write("                var p = $(e).parentsUntil(\"ul\").parent().find(\"[name=param]\");\r\n");
      out.write("                var _ps = [];\r\n");
      out.write("                p.each(function (_i, _e) {\r\n");
      out.write("                    var _val = $(_e).siblings(\"input\").val();\r\n");
      out.write("                    if ($.trim(_val).length > 0) {\r\n");
      out.write("                        _ps.push(_val);\r\n");
      out.write("                    }\r\n");
      out.write("                });\r\n");
      out.write("                var _val = $(e).siblings(\"input\").val();\r\n");
      out.write("                if ($.trim(_val).length > 0 && _ps.length > 0) {\r\n");
      out.write("                    params.push({\r\n");
      out.write("                        \"group\": _val,\r\n");
      out.write("                        \"params\": _ps\r\n");
      out.write("                    });\r\n");
      out.write("                }\r\n");
      out.write("            });\r\n");
      out.write("            var url = \"/item/param/save/\" + $(\"#itemParamAddTable [name=cid]\").val();\r\n");
      out.write("            $.post(url, {\"paramData\": JSON.stringify(params)}, function (data) {\r\n");
      out.write("                if (data.status == 200) {\r\n");
      out.write("                    $.messager.alert('提示', '新增商品规格成功!', undefined, function () {\r\n");
      out.write("                        $(\".panel-tool-close\").click();\r\n");
      out.write("                        $(\"#itemParamList\").datagrid(\"reload\");\r\n");
      out.write("                    });\r\n");
      out.write("                }\r\n");
      out.write("            });\r\n");
      out.write("        });\r\n");
      out.write("    });\r\n");
      out.write("</script>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
