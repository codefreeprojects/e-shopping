package com.online.shopping.services;

import com.online.shopping.dao.*;
import com.online.shopping.dto.CreateOrderReqDTO;
import com.online.shopping.dto.CreateProductReqDTO;
import com.online.shopping.dto.PaginationDTO;
import com.online.shopping.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ProductDAO productDAO;

    @Autowired
    ProductOrderDAO productOrderDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    PaymentDAO paymentDAO;
    @Autowired
    FeedbackDAO feedbackDAO;
    @Autowired
    FilesStorageServiceImpl filesStorageService;

    @Value("${files.upload.url}")
    String filePath;
    @Transactional
    public Optional<Product> createProduct(CreateProductReqDTO reqDTO) {
        Optional<String> fileName = filesStorageService.save(reqDTO.getBannerImage());
        if(fileName.isEmpty())
            return Optional.empty();
        Product product = new Product();
        product.setName(reqDTO.getName());
        product.setPrice(reqDTO.getPrice());
        product.setCompany(reqDTO.getCompany());
        product.setDetails(reqDTO.getDetails());
        product.setType(reqDTO.getType());
        product.setImageUrl(filePath+fileName.get());
        productDAO.save(product);
        return Optional.of(product);
    }

    public Page<Product> getAllProducts(PaginationDTO req){
        return productDAO.findAll(PageRequest.of(req.getPage()-1,req.getSize() ));
    }
    public Product updateProduct(Product product){
         productDAO.save(product);
         return product;
    }

    @Transactional
    public void deleteProduct(Long productId){
        Optional<Product> _product = productDAO.findById(productId);
        if(_product.isPresent()){
            Product product = _product.get();
            feedbackDAO.deleteAllByProduct(product);
            productOrderDAO.deleteAllByProduct(product);
            productDAO.delete(product);
        }
    }


}
