package com.cooloongwu.crawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 抓取淘宝宝贝详情页的图片
 * Created by CooLoongWu on 2017-11-22.
 */
public class TaoBaoCrawler implements PageProcessor {

    // 步骤一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    //    private static String url = "https://detail.tmall.com/item.htm?spm=a1z10.1-b-s.w4004-17359264940.2.5c666ca3hygwhu&id=530425267557&skuId=3160253316569";
    private static String url = "https://detail.tmall.com/item.htm?spm=a222r.10352565.5717542303.1.1eb8bde2ePnm8G&acm=lb-zebra-267775-2506068.1003.4.2278352&id=539386214836&scm=1003.4.lb-zebra-267775-2506068.ITEM_539386214836_2278352";

    private static String basePath = "D:\\Temp\\Test\\";
    private static String imgPath = "文件夹名称";

    public static void main(String[] args) {

        Spider.create(new TaoBaoCrawler())
                //从"该url"开始抓
                .addUrl(url)
                //保存结果
                //.addPipeline(new JsonFilePipeline(imgPath))
                //.setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe"))
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }

    public void process(Page page) {
        // 动态加载网页
        // 第一步：指定驱动位置
        System.setProperty("webdriver.chrome.driver",
                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        // 第二步：初始化驱动
        WebDriver driver = new ChromeDriver();
        // 第三步：获取目标网页
        driver.get(url);
        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 第四步：解析
        Html html = new Html(driver.getPageSource());
        driver.close();

        // 步骤二：定义如何抽取页面信息，并保存下来
        //page.putField("标题", html.xpath("//div[@class='tb-detail-hd']/h1/text()").toString());
        //page.putField("imgUrl", html.xpath("//div[@class='content ke-post']/p").css("img", "src").toString());

        List<String> imgUrls = html.xpath("//div[@class='content ke-post']").css("img", "src").all();
        if (!imgUrls.isEmpty()) {
            for (int i = 0; i < imgUrls.size(); i++) {
                //System.out.println("图片地址：" + imgUrl);
                //page.putField("抓取详情图" + (i + 1), imgUrls.get(i));

                String imgUrl = imgUrls.get(i);
                //System.out.println("抓取详情图地址" + (i + 1) + "：" + imgUrl);
                String[] temp = imgUrl.split("\\.");
                String imgType = temp[temp.length - 1];
                try {
                    DownloadImage.download(imgUrls.get(i), "抓取的详情图" + (i + 1) + "." + imgType, basePath + imgPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("图片下载错误！");
                }

            }
        } else {
            System.out.println("未爬取到图片！");
        }

    }

    public Site getSite() {
        return site;
    }
}
