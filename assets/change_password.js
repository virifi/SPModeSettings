js_scraper.scrape({
    "setting/$" : function () {
        js_scraper.pagestep(3, 1);
        js_scraper.log("matched : Toppage");
        var link_url = $('a[href *= "passwordChgClean"]').attr('href');
        if (link_url != null) {
            js_scraper.load_url(link_url);
        } else {
            js_scraper.scraping_error("ページの解析に失敗しました。\n\nエラーページ\nTop page");
            js_scraper.print_source();
        }
    },
    "setting/passwordChgClean" : function () {
        js_scraper.pagestep(3, 2);
        var prev_password = js_scraper.get_argument("prev_password");
        var new_password = js_scraper.get_argument("new_password");
        if (prev_password == null || new_password == null) {
            js_scraper.scraping_error("暗証番号の取得に失敗しました");
            return;
        }
        $('input[name = "xmodePassword"]').val(prev_password);
        $('input[name = "newXmodePassword"]').val(new_password);
        $('input[name = "newXmodePasswordConf"]').val(new_password);
        $('form').submit();
    },
    "setting/passwordChgRegAction" : function () {
        js_scraper.pagestep(3, 3);
        var body = $('body').text();
        if (body.match(new RegExp("パスワードを変更しました"))) {
            js_scraper.scraping_finished("パスワードを変更しました");
        } else {
            js_scraper.scraping_error(body);
            js_scraper.print_source();
        }
    }
}, function () {
    js_scraper.scraping_error("不明なURLです。\n" + url);
    js_scraper.print_source();
});
