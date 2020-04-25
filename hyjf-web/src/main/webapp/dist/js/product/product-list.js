    $(".timeout").each(function() {
        var ele = $(this),
            s = parseInt(ele.data("end")) - parseInt(ele.data("start"));
        utils.timer(ele, s);
    })
