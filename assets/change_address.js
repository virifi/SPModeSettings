js_scraper.scrape({
    "setting/$" : function () {
        js_scraper.pagestep(4, 1);
        js_scraper.log("matched : Toppage");
        var link_url = $('a[href *= "mailMenuAction"]').attr('href');
        if (link_url != null) {
            js_scraper.load_url(link_url);
        } else {
            js_scraper.scraping_error("ページの解析に失敗しました。\n\nエラーページ\nTop page");
            js_scraper.print_source();
        }
    },
    "mail_setting/mailMenuAction" : function () {
        js_scraper.pagestep(4, 2);
        js_scraper.log("matched : mailMenuAction");
        var link_url = $('a[href *= "mailAddressClean"]').attr('href');
        if (link_url != null) {
            js_scraper.load_url(link_url);
        } else {
            js_scraper.scraping_error("ページの解析に失敗しました。\n\nエラーページ\nmailMenuAction");
            js_scraper.print_source();
        }
    },
    "mail_setting/mailAddressClean" : function () {
        js_scraper.pagestep(4, 3);
        js_scraper.log("matched : mailAddressClean");
        var address = js_scraper.get_argument("address");
        var password = js_scraper.get_argument("password");

        if (address == null) {
            js_scraper.scraping_error("メールアドレスの取得に失敗しました。");
            return;
        }
        if (password == null) {
            js_scraper.scraping_error("暗証番号の取得に失敗しました。");
            return;
        }
        $('input[name = "xmailAddr"]').val(address);
        $('input[name = "xmodePassword"]').val(password);
        $('form').submit();
    },
    "mail_setting/mailAddressRegAction" : function () {
        function match(str, regexp_str) {
            return str.match(new RegExp(regexp_str));
        }
        js_scraper.pagestep(4, 4);
        js_scraper.log("matched : mailAddressRegAction");
        var body = $('body').text();
        if (match(body, "あなたの新しいメールアドレスは")) {
            js_scraper.scraping_finished("メールアドレスの変更が正常に完了しました");
        } else if (match(body, "spモードパスワードの入力に誤りがあります")
                || match(body, "spモードパスワードが間違っています")) {
            js_scraper.scraping_error("入力された暗証番号は誤っています。");
        } else if (match(body, "入力されたメールアドレスは既に使われています")) {
            js_scraper.scraping_error("入力されたメールアドレスは既に使用されています。");
        } else {
            js_scraper.scraping_error(body);
            js_scraper.print_source();
        }
    }
}, function () {
    js_scraper.scraping_error("不明なURLです。\n" + url);
    js_scraper.print_source();
});
