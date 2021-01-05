package ec.im.webServer.base.exception;


import ec.im.webServer.base.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData<String> baseErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ResponseData<String> result = new ResponseData<String>();

        logger.error("Exception Handler：Host {} invokes url {} ERROR: {}", req.getRemoteHost(), req.getRequestURL(), e.getMessage());
        logger.error("process is error!", e);

        if (ApplicationException.class.isAssignableFrom(e.getClass())) {
            ApplicationException applicationException = (ApplicationException) e;
            result = new ResponseData<String>(applicationException.getAppCode());
            result.setMsg(result.getMsg());
        } else if (MethodArgumentNotValidException.class.isAssignableFrom(e.getClass())) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            result = new ResponseData<String>();
            result.setMsg(methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } else {
            result = new ResponseData<String>();
            result.setMsg(ResponseCode.UNKOWN_EXCEPTION.getMessage());
        }
//        Context context =  ContextUtils.get();
//        if(context != null){
//            result.setTraceId(context.getTraceId());
//        }
        return result;
    }
}