package filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CustomLogFilter implements Filter {
    private StringBuilder requestBuilderLogs;
    private StringBuilder responseBuilderLogs;

    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification, FilterableResponseSpecification filterableResponseSpecification, FilterContext filterContext) {
        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
        requestBuilderLogs = new StringBuilder();
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("Request method: " + objectValidation(filterableRequestSpecification.getMethod()));
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("Request URI: " + objectValidation(filterableRequestSpecification.getURI()));
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("Form Params: " + objectValidation(filterableRequestSpecification.getFormParams()));
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("Request Param: " + objectValidation(filterableRequestSpecification.getRequestParams()));
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("Headers: " + objectValidation(filterableRequestSpecification.getHeaders()));
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("Cookies: " + objectValidation(filterableRequestSpecification.getCookies()));
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("Proxy: " + objectValidation(filterableRequestSpecification.getProxySpecification()));
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("Body: " + objectValidation(filterableRequestSpecification.getBody()));
        requestBuilderLogs.append("\n");
        requestBuilderLogs.append("******************************");
        responseBuilderLogs = new StringBuilder();
        responseBuilderLogs.append("\n"+"\n"+"\n");
        responseBuilderLogs.append("Status Code: "+response.getStatusCode());
        responseBuilderLogs.append("\n");
        responseBuilderLogs.append("Status Line: "+response.getStatusLine());
        responseBuilderLogs.append("\n");
        responseBuilderLogs.append("Response Cookies: "+response.getDetailedCookies());
        responseBuilderLogs.append("\n");
        responseBuilderLogs.append("Response Content Type: "+response.getContentType());
        responseBuilderLogs.append("\n");
        responseBuilderLogs.append("Response Headers: "+response.getHeaders());
        responseBuilderLogs.append("\n");
        responseBuilderLogs.append("Response Body: "+"\n"+response.getBody().prettyPrint());
        return response;
    }

    public String getRequestBuilder() {
        return requestBuilderLogs.toString();
    }

    public String getResponseBuilder() {
        return responseBuilderLogs.toString();
    }

    public String objectValidation(Object o) {
        if (o == null)
            return null;
        else
            return o.toString();
    }

}
