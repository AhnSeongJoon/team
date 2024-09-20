package com.shop.service;

import com.shop.dto.ProductDto;
import com.shop.entity.Product;
import com.shop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 크롤링된 상품을 DB에 저장하는 메서드
    public Product saveProduct(ProductDto productDto) {
        Product product = new Product(
                productDto.getName(),
                productDto.getImgUrl(),
                productDto.getPrice(),
                productDto.getCategory()
        );
        return productRepository.save(product);
    }

    // 크롤링한 데이터를 가져오고 DB에 저장하는 메서드
    public List<Product> getProductDatas() {
        // ChromeDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless"); // 헤드리스 모드로 브라우저 창을 띄우지 않음

        WebDriver driver = new ChromeDriver(options);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<Product> productList = new ArrayList<>();

        try {
            driver.get("https://www.ost.co.kr/store/ranking/list?categoryIdx=1&pageNo=0");

            int desiredItemCount = 100; // 가져올 상품 개수를 10개로 제한
            int loadedItemCount = 0;

            while (loadedItemCount < desiredItemCount) {
                List<WebElement> products = driver.findElements(By.cssSelector("ul#ulProductList > li.prdList__item"));
                for (WebElement product : products) {
                    if (loadedItemCount >= desiredItemCount) {
                        break; // 필요한 만큼의 상품을 가져오면 중지
                    }

                    String productName = product.findElement(By.cssSelector("a[name='link_view']")).getAttribute("data-style-nm");
                    String productPrice = product.findElement(By.cssSelector("div.prdItem__price > p")).getText();
                    String imgUrl = product.findElement(By.cssSelector(".prdItem__pic")).getCssValue("background-image")
                            .replace("url(\"", "").replace("\")", "");

                    // 카테고리 설정
                    String category = categorizeProduct(productName);

                    Product productObj = new Product(productName, imgUrl, productPrice, category);
                    productList.add(productObj);

                    productRepository.save(productObj);  // DB에 저장

                    loadedItemCount++; // 가져온 상품 개수 증가
                }

                js.executeScript("window.scrollTo(0, document.body.scrollHeight);"); // 스크롤을 하여 더 많은 상품 로드
                Thread.sleep(1000); // 페이지가 로드되는 시간을 확보하기 위해 잠시 대기
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return productList;
    }

    // 상품명을 기준으로 카테고리 분류
    private String categorizeProduct(String productName) {
        if (productName.contains("반지") || productName.contains("커플링")) {
            return "반지";
        } else if (productName.contains("팔찌")) {
            return "팔찌";
        } else if (productName.contains("목걸이")) {
            return "목걸이";
        } else if (productName.contains("귀걸이")) {
            return "귀걸이";
        } else {
            return "기타";
        }
    }

    // 크롤링된 상품을 ID로 조회
    public ProductDto getCrawledProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getImgUrl(),
                product.getPrice(),
                product.getCategory()
        );
    }
}
