/**
 * Created by Administrator on 2014/8/20.
 */

/**
 * @author Frendy
 * @Funtion 截取日期的一部分，如输入date：1991-05-18，type：year 则返回1991
 * @param date 格式为xxxx-xx-xx 或 xxxx-x-xx 或 xxxx-xx-x 或 xxxx-x-x
 * @param type 有year，month，day，分别对应返回年，月，日
 * @returns {*} 返回年，月，日
 */
function getDatePart(date,type){
    if(true == validate(date,"date")){
        var year,month,day;
        year = date.substr(0,4);
        if(date.substr(6,1)=='-'){
            month = date.substr(5,1);
            day = date.substr(7,date.length-7);
        }
        else{
            month = date.substr(5,2);
            day = date.substr(8,date.length-8);
        }
        if(day.length==1){
            day = '0'+day;
        }
        if(month.length==1){
            month = '0'+month;
        }
        if(type == 'year'){
            return year;
        }
        if(type == 'month'){
            return month;
        }
        if(type == 'day'){
            return day;
        }
    }
    return false;
}

/**
 * @author Frendy
 * @Function 比较日期一，日期二大小，返回日期比较大的日期，当相等的时候返回date2
 *           如输入date1：1991-6-15，date2：1992-5-16 返回1992-5-16
 * @param date1 日期一 格式满足xxxx-xx-xx 或 xxxx-x-xx 或 xxxx-xx-x 或 xxxx-x-x
 * @param date2 日期二 格式满足xxxx-xx-xx 或 xxxx-x-xx 或 xxxx-xx-x 或 xxxx-x-x
 * @returns {*}
 */
function getBigDate(date1,date2){
    if(true == validate(date1,'date') && true == validate(date2,'date')){
        if(getDatePart(date1,'year')<getDatePart(date2,'year')){
            return date2;
        }
        if(getDatePart(date1,'year')>getDatePart(date2,'year')){
            return date1;
        }
        if(getDatePart(date1,'year')==getDatePart(date2,'year')){
            if(getDatePart(date1,'month')<getDatePart(date2,'month')){
                return date2;
            }
            if(getDatePart(date1,'month')>getDatePart(date2,'month')){
                return date1;
            }
            if(getDatePart(date1,'month')==getDatePart(date2,'month')){
                if(getDatePart(date1,'day')<getDatePart(date2,'day')){
                    return date2;
                }
                if(getDatePart(date1,'day')>getDatePart(date2,'day')){
                    return date1;
                }
                if(getDatePart(date1,'day')== getDatePart(date2,'day')){
                    return date2;
                }
            }
        }
    }
    return 'error';
}

/**
 * @author Frendy
 * @Function 输入日期后统一输出日期格式如xxxx-xx-xx 如输入2005-5-8 返回2005-05-08
 * @param date 输入格式xxxx-xx-xx 或 xxxx-x-xx 或 xxxx-xx-x 或 xxxx-x-x
 * @returns 返回日期格式xxxx-xx-xx
 * @change 2014/9/23
 * @changePart 修复=号至==
 */
function transferDateFormat(date){
    //?XXXX-X-X???XXXX-XX-XX
    var year,month,day;
    year = parameter.substr(0,4);
    if(year<0) return false;
    if(parameter.substr(6,1)=='-'){
        month = parameter.substr(5,1);
        day = parameter.substr(7,parameter.length-7);
    }
    else{
        month = parameter.substr(5,2);
        day = parameter.substr(8,parameter.length-8);
    }
    if(day.length==1){
        day = '0'+day;
    }
    if(month.length==1){
        month = '0'+month;
    }
    date = year + '-' + month + '-' + day;
    return date;
}

/**
 * @author Frendy
 * @Function 检测输入参数的格式是否匹配type类型 如输入parameter：1685-5-9，type：date，返回true
 * @param parameter 输入参数]
 * @param type 参数类型分别有\br
 *              date：日期匹配（运算匹配，能检测闰年，推荐使用）\br
 *              dateSimple：日期匹配（简单正则表达式检验，不能检测闰年）\br
 *              dateExtreme：日期匹配（复杂正则表达式检验，能检测闰年）\br
 *              encoding：编码检测（是否满足类似这种A344-H098格式）\br
 *              name：名字检测（必须包含中文或英文或_()#$%*<>[]{}+"组成"）\br）
 *              ename：英文名检测（必须包含英文，可由英文和符号_()#$%*<>[]{}"+组成"）\br
 *              cname: 中文名检测（必须包含中文，由中英文和符号_()#$%*<>[]{}"+组成）\br
 *              price: 价格检测 （可以为小数，不能为负数）\br
 *              option: 选项内容检测 （为空字符串，返回false）\br
 * @returns {boolean} 符合条件返回te，不符合返回false
 */
function validate(parameter,type){
    var re;
    //1991-18-6
    if(type == 'date'){
        //xxxx-xx-xx
        re = /^\d{4}-\d{1,2}-\d{1,2}$/;
        if(re.test(parameter)){
            var year,month,day;
            year = parameter.substr(0,4);
            if(year<0) return false;
            if(parameter.substr(6,1)=='-'){
                month = parameter.substr(5,1);
                day = parameter.substr(7,parameter.length-7);
            }
            else{
                month = parameter.substr(5,2);
                day = parameter.substr(8,parameter.length-8);
            }
            if(day.length==1){
                day = '0'+day;
            }
            if(month.length==1){
                month = '0'+month;
            }
            if(0==year%4 && 0!=year%100 || 0==year%400){
                if(month>=1 && month<=12){
                    if(1==month||3==month||5==month||7==month||8==month||10==month||12==month){
                        if(day>=1 && day<=31){
                            return true;
                        }else{
                            return false;
                        }
                    }
                    else if(2==month){
                        if(day>=1 && day<=29){
                            return true;
                        }else{
                            return false;
                        }
                    }
                    else{
                        if(day>=1 && day<=30){
                            return true;
                        }else{
                            return false;
                        }
                    }
                }
            }
            else{
                if(month>=1 && month<=12){
                    if(1==month||3==month||5==month||7==month||8==month||10==month||12==month){
                        if(day>=1 && day<=31){
                            return true;
                        }else{
                            return false;
                        }
                    }
                    else if(2==month){
                        if(day>=1 && day<=28){
                            return true;
                        }else{
                            return false;
                        }
                    }
                    else{
                        if(day>=1 && day<=30){
                            return true;
                        }else{
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    if(type == 'dateSimple'){
        re = /^([1-2]\d{3})-(0?[1-9]|10|11|12)-([1-2]?[0-9]|0[1-9]|30|31)$/;
        if(re.test(parameter)) {
            alert("true");
            return true;
        }
        else return false;
    }

    if(type == 'dateExtreme'){
        re = /((^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(10|12|0?[13578])([-\/\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(11|0?[469])([-\/\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(0?2)([-\/\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([3579][26]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][13579][26])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][13579][26])([-\/\._])(0?2)([-\/\._])(29)$))/;
        if(re.test(parameter)) {
            alert("true");
            return true;
        }
        else return false;
    }

    if(type == 'encoding'){
        //A344-H098
        re = /^[A-Za-z0-9]+$/;
        if(re.test(parameter)) return true;
        else return false;
    }

    if (type=='name'){
        re = /^[\u2E80-\u9FFF\w\(\)\%\$\#\*\<\>\[\]\{\}\"\+]+$/;
        if(re.test(parameter)) return true;
        else return false;
    }

    if(type == 'cname'){
        //字母数字_()#$%*<>[]{}"
        re = /^[\u2E80-\u9FFF\w\(\)\%\$\#\*\<\>\[\]\{\}\"\+]*[\u2E80-\u9FFF]+[\u2E80-\u9FFF\w\(\)\%\$\#\*\<\>\[\]\{\}\"\+]*$/;
        if(re.test(parameter)) return true;
        else return false;
    }

    if(type == 'ename'){
        //字母数字_()#$%*<>[]{}"
        re = /^[\w\(\)\$\%\#\*\<\>\[\]\{\}\"\+]*[a-zA-Z]+[\w\(\)\$\%\#\*\<\>\[\]\{\}\"\+]*$/;
        if(re.test(parameter)) return true;
        else return false;
    }

    if(type == 'price'){
        re = /^[0-9]{0,13}\.{0,1}([0-9]{1,2})$/;
        if(re.test(parameter)) return true;
        return false;
    }
    if(type == 'option'){
        if(!(!parameter)) return true;
        else return false;
    }
    if(type == 'number') {
        re = /^[0-9]+$/;
        if (re.test(parameter)) return true;
        return false;
    }
    if(type == 'notnull'){
        if(parameter=="") return false;
        else return true;
    }
    if(type == 'null'){
        if(parameter=="") return true;
        else return false;
    }

    return false;
}

var wrong_background_css ={
    background:'#FFDAD4'
};

var right_background_css ={
    background:'white'
};

/**
 * 检查表单的值是否为空
 * @param checkArray 表单的选择器表达式
 * @param valueArray 等于特定值也视为空
 * @return 空返回false 非空返回true
 */
function checkNull(checkArray,valueArray){
    var isSuccess = true;
    $.each(checkArray,function(index,element){
        var obj = $(element);
        if(!obj||!obj.val()||(!(!valueArray)&&obj.val()==valueArray[index])){
            obj.css("background",wrong_background_css.background);
            isSuccess = false;
        }else{
            obj.css("background",right_background_css.background);
        }
    });
    return isSuccess;
}