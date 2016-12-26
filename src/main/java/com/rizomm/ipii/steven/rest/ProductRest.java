package com.rizomm.ipii.steven.rest;

import com.rizomm.ipii.steven.dao.ICategoryDao;
import com.rizomm.ipii.steven.dao.IProductDao;
import com.rizomm.ipii.steven.dao.ProductDao;
import com.rizomm.ipii.steven.helper.Utils;
import com.rizomm.ipii.steven.model.Product;
import org.codehaus.jettison.json.JSONObject;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by steven on 17/11/2016.
 */

@Path("/product")
@Named
@RequestScoped
public class ProductRest {

    @EJB
    private IProductDao PD;

    @EJB
    private ICategoryDao CD;

    @POST
    @Produces("application/json")
    public Response addProduct(String productString) {
        Map<String, Object> result = PD.convertJsonToProduct(productString,CD);
        if(!((boolean) result.get("ERROR"))){
            Product product = PD.createProduct((Product) result.get("PRODUCT"));
            result = Utils.generateMessageSuccess201("Product créé avec l'id : " + product.getId());
        }

        return Response.status((int) result.get("CODE_HTTP")).entity(result.get("MESSAGE_HTTP")).build() ;
    }

    @GET
    @Path("/{numberPage : \\d+}/page")
    @Produces("application/json")
    public Response getAllProduct(String productString,@PathParam("numberPage") int numberPage) {
        int start = numberPage * 9;
        int limit = (numberPage + 1 * 9) - 1;
        List<Product> listProduct = PD.findAllProductByPage(start,limit);

        JSONObject jsonProducts = PD.convertProductsToJson(listProduct);

        Map<String, Object> result = Utils.generateMessageSuccess200(jsonProducts);

        return Response.status((int) result.get("CODE_HTTP")).entity(result.get("MESSAGE_HTTP")).build() ;
    }

    @GET
    @Path("/count")
    @Produces("application/json")
    public Response getCountAllProduct(String productString) {
        List<Product> listProduct = PD.countAllProduct();

        JSONObject jsonProducts = PD.convertProductsToJson(listProduct);

        Map<String, Object> result = Utils.generateMessageSuccess200(jsonProducts);

        return Response.status((int) result.get("CODE_HTTP")).entity(result.get("MESSAGE_HTTP")).build() ;
    }

}
