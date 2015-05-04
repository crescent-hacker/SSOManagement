function json2str(obj)
{
    var S = [];
    for(var i in obj){
        obj[i] = typeof obj[i] == "string"?"'"+obj[i]+"'":(typeof obj[i] == "object"?json2str(obj[i]):obj[i]);
        S.push("'"+i+"':"+obj[i]);
    }
    return "{"+S.join(",")+"}";
}