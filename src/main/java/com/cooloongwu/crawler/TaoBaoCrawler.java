package com.cooloongwu.crawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * 抓取淘宝宝贝详情页的图片
 * Created by CooLoongWu on 2017-11-22.
 */
public class TaoBaoCrawler implements PageProcessor {

    // 步骤一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    private static String url = "https://detail.tmall.com/item.htm?spm=a1z10.1-b-s.w4004-17359264940.2.5c666ca3hygwhu&id=530425267557&skuId=3160253316569";


    public static void main(String[] args) {
        Spider.create(new TaoBaoCrawler())
                //从"该url"开始抓
                .addUrl(url)
                //保存结果
                //.addPipeline(new JsonFilePipeline("D:\\Temp\\Test\\"))
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }

    public void process(Page page) {

        System.setProperty("webdriver.chrome.driver",
                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        // 第二步：初始化驱动
        WebDriver driver = new ChromeDriver();
        // 第三步：获取目标网页
        driver.get(url);
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 第四步：解析。以下就可以进行解了。使用webMagic、jsoup等进行必要的解析。
        Html html = new Html(driver.getPageSource());
        driver.close();

        // 步骤二：定义如何抽取页面信息，并保存下来
        page.putField("name", html.xpath("//div[@class='tb-detail-hd']/h1/text()").toString());
        page.putField("imgUrl", html.xpath("//div[@class='content ke-post']/p").css("img", "src").toString());
    }

    public Site getSite() {
        return site;
    }
}
