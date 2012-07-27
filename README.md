SPModeSettings
spモードのデフォルトの暗証番号「0000」がどれだけ危険であるかを示すためのアプリです。 
spモードのメールアドレスと暗証番号を変更することができます。 
使用する場合は、spモード網に接続の上ご使用ください。Wifi経由では使用できません。

### ダウンロード
* [SPModeSettings.apk](https://github.com/downloads/virifi/SPModeSettings/SPModeSettings.apk)  

ご使用になるにはこのアプリにspモードの暗証番号を入力する必要があります。 
お試しになる場合はご自分でビルドされることをおすすめします。

### パーミッション
android.permission.INTERNETのみ

### スクリーンショット
![スクリーンショット](https://github.com/virifi/SPModeSettings/raw/master/readme_imgs/spmodesettings1.png)

### ライセンス
 Copyright (C) 2012 virifi 

 Licensed under the Apache License, Version 2.0 (the "License"); 
 you may not use this file except in compliance with the License. 
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software 
 distributed under the License is distributed on an "AS IS" BASIS, 
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 See the License for the specific language governing permissions and 
 limitations under the License. 

### 解説
spモードのパスワード変更を例に説明します。 
spモードのパスワードを変更するには、https://spmode.ne.jp/setting/ （一番左の画像）にアクセスし、「spモードパスワード変更」をクリックします。 
そのリンク先（中央の画像）で現パスワードと新パスワードを入力し、決定ボタンを押すとパスワード変更が完了します。 
![公式サイト設定画面](https://github.com/virifi/SPModeSettings/raw/master/readme_imgs/spmodesettings2.png) 
このパスワード変更の過程で必要なのは、spモード網に接続されているということと4桁の暗証番号のみでユーザー名などは必要ありません。 
つまり、暗証番号さえわかっていれば、spモードに接続中に、上記の変更処理をプログラムによって自動的に行わせることにより暗証番号が変更できてしまうわけです。 
普通は暗証番号はわからないはずですが、spモードの場合、**ユーザーが特に変更処理を行わない限り暗証番号は「0000」になっているのです。 
したがって、spモードの暗証番号を変更していない人はマルウェアによってspモードの設定を不正に変更されてしまう恐れがあるのです。**


本アプリはメールアドレスとパスワード変更処理を自動的に行うことができることを実証するために作成しました。 
本アプリでは上記の処理を画面に表示しないWebViewとJavaScriptを組み合わせて実現しています。
Java側では[ScrapingFragment.java](https://github.com/virifi/SPModeSettings/blob/master/src/net/virifi/android/spmodesettings/ScrapingFragment.java)が変更処理を担っています。 
ScrapingFragment#startScrapingメソッドは引数にスクレイピングの起点となるurlとスクレイピング処理が記述されたJavaScriptファイルを受け取ります。 
そしてScrapingFragmentは起点となるURLを読み込み、その読み込みが完了後、jquery-1.7.2.min.js、js_scraper.js、そして渡されたメインのスクリプトを実行します。 
メインのスクリプトでは、jQueryを使って移動したいリンク先を探しそのリンク先に移動します。 
すると、ScrapingFragmentは移動先のページ読み込み終了後、再びjQuery, js_scraper,メインのスクリプトの読み込みを行います。 
あとはそれを繰り返すことにより上記の処理が実現できるわけです。 
メールアドレス変更処理は[change_address.js](https://github.com/virifi/SPModeSettings/blob/master/assets/change_address.js)が、そしてパスワード変更処理は[change_password.js](https://github.com/virifi/SPModeSettings/blob/master/assets/change_password.js)が行なっています。
