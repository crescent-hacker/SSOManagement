<#if 0<totalPages >
<div class="row">
    <div class="col-lg-4">
        <ul class="pagination">
            <li>
                <div class="col-xs-2">
                    <input name="pageToGo" id="pageToGo" type="text" class="form-control" style="text-align: center;" value="${pageIndex?c}">
                </div>
                <button id="goPageButton" class="btn btn-primary" type="button"
                        onclick="gotoPage($('input[name=\'pageToGo\']').val())">跳转
                </button>
                &nbsp;&nbsp;&nbsp;共 ${totalPages?c}页<#if records??>，${records?c}条</#if>
            </li>
        </ul>
    </div>
    <div class="col-lg-6">
        <ul class="pagination">
        <#--上一页-->
            <li <#if pageIndex == 1>class="disabled"</#if>>
                <a href="javascript:void(0)" onclick="prevPage(<#if pageIndex == 1>1<#else>${pageIndex-1}</#if>)">
                    上一页
                </a>
            </li>
        <#--页数陈列-->
        <#if totalPages<=5>
            <#list 1..totalPages as index>
                <#if index == pageIndex>
                    <li class="active">
                        <a href="javascript:void(0)" onclick="gotoPage(${index?c})">
                        ${index?c} <span class="sr-only">(current)</span>
                        </a>
                    </li>
                <#else>
                    <li>
                        <a href="javascript:void(0)" onclick="gotoPage(${index?c})">
                        ${index?c}
                        </a>
                    </li>
                </#if>
            </#list>
        <#elseif pageIndex-2<=1>
            <#list 1..5 as index>
                <#if index == pageIndex>
                    <li class="active">
                        <a href="javascript:void(0)" onclick="gotoPage(${index?c})">
                        ${index?c} <span class="sr-only">(current)</span>
                        </a>
                    </li>
                <#else>
                    <li>
                        <a href="javascript:void(0)" onclick="gotoPage(${index?c})">
                        ${index?c}
                        </a>
                    </li>
                </#if>
            </#list>
        <#elseif (pageIndex+2>=totalPages)>
            <#list totalPages-4..totalPages as index>
                <#if index == pageIndex>
                    <li class="active">
                        <a href="javascript:void(0)" onclick="gotoPage(${index?c})">
                        ${index?c} <span class="sr-only">(current)</span>
                        </a>
                    </li>
                <#else>
                    <li>
                        <a href="javascript:void(0)" onclick="gotoPage(${index?c})">
                        ${index?c}
                        </a>
                    </li>
                </#if>
            </#list>
        <#else>
            <#list pageIndex-2..pageIndex+2 as index>
                <#if index == pageIndex>
                    <li class="active">
                        <a href="javascript:void(0)" onclick="gotoPage(${index?c})">
                        ${index?c} <span class="sr-only">(current)</span>
                        </a>
                    </li>
                <#else>
                    <li>
                        <a href="javascript:void(0)" onclick="gotoPage(${index?c})">
                        ${index?c}
                        </a>
                    </li>
                </#if>
            </#list>
        </#if>
        <#--下一页-->
            <li <#if pageIndex == totalPages>class="disabled"</#if>>
                <a href="javascript:void(0)"
                   onclick="nextPage(<#if pageIndex == totalPages>${totalPages}<#else>${pageIndex+1}</#if>)">
                    下一页
                </a>
            </li>

        </ul>
    </div>

    <div class="col-lg-2">

    </div>
</div>
<input type="hidden" name="totalPages" value="${totalPages}">
</#if>