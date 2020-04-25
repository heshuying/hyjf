//格式化 “基本信息” 数据
if (typeof baseTableData != "undefined") {
    var baseTableHtml = normalFormat(baseTableData);
    $("#baseTable").html(baseTableHtml);
}
//格式化 “资产信息” 数据
if (typeof assetsTableData != "undefined") {
    var assetsTableHtml = repeatFormat(assetsTableData);
    $("#assetsTable").html(assetsTableHtml);
}
//格式化 “项目介绍” 数据
if (typeof intrTableData != "undefined") {
    var intrTableHtml = normalFormat(intrTableData);
    $("#intrTable").html(intrTableHtml);
}

//格式化 “信用状况” 数据
if (typeof credTableData != "undefined") {
    var credTableHtml = normalFormat(credTableData);
    $("#credTable").html(credTableHtml);
}

//格式化 “审核状况” 数据
if (typeof reviewTableData != "undefined") {
    var reviewTableHtml = normalFormat(reviewTableData);
    $("#reviewTable").html(reviewTableHtml);
}

//格式化 “其他信息” 数据
if (typeof otherTableData != "undefined") {
    var otherTableHtml = normalFormat(otherTableData);
    $("#otherTable").html(otherTableHtml);
}

function normalFormat(data) {
    var order = 0; //排序所用标记
    var str = "<table  cellpadding='0' cellspacing='0'>";
    for (var i = 0; i < data.length; i++, order++) {
        var even = order % 2 == 0;
        var odd = order % 2 == 1;
        var isOneLine = _isOneLine(i);
        if (isOneLine) {
            //独占一行
            str += "<tr>";
            str += "<td colspan='2' width='100%'>";
            str += "<span class='key'>" + data[i].key + "</span> <span class='val'>" + data[i].val + "</span></td>";
            str += "</tr>";
            order = 1; //重置排序
        } else {
            //非独占一行
            if (even) {
                str += "<tr>";
            }
            if (i == data.length - 1 && even) {
                //最后一条数据若是在第一个td，则跨2列
                str += "<td colspan='2' width='100%'>";

            } else {
                str += "<td width='50%'>";
            }
            str += "<span class='key'>" + data[i].key + "</span> <span class='val'>" + data[i].val + "</span></td>";
            if (even && _isOneLine(i + 1)) {
                str += "<td></td>";
            }
            if (odd) {
                str += "</tr>";
            }
        }

    }
    str += "</table>";
    return str;

    function _isOneLine(i) {
        //判断是否占用一行
        if (data[i] == undefined) {
            return false;
        }
        //占一行 的情况
        return data[i].id == "borrowContents" //项目信息
            || data[i].id == "fianceCondition" //财务状况 
	        || data[i].id == "position"; //岗位职业 
    }
}


function repeatFormat(dataall) {
    var str = "";
    for (var a = 0; a < dataall.length; a++) {
        if (a > 0) {
            str += "<div class='cutline'></div>";
        }
        str += "<table  cellpadding='0' cellspacing='0'>"
        var data = dataall[a];
        for (var i = 0; i < data.length; i++) {
            var even = i % 2 == 0;
            var odd = i % 2 == 1;
            if (even) {
                str += "<tr>";
            }
            if (i == data.length - 1 && even) {
                str += "<td colspan='2' width='100%'>";
            } else {
                str += "<td width='50%'>";
            }
            str += "<span class='key'>" + data[i].key + "</span> <span class='val'>" + data[i].val + "</span></td>";
            if (odd) {
                str += "</tr>";
            }
        }
        str += "</table>"
    }
    return str;
}
