package com.simon.controller;

import com.simon.common.controller.BaseController;
import com.simon.common.domain.ResultCode;
import com.simon.common.domain.ResultMsg;
import com.simon.model.VeriCode;
import com.simon.repository.VeriCodeRepository;
import com.simon.service.SmsService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.domain.BizResult;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Created by simon on 2016/9/19.
 */
@Api(value = "验证码", description = "验证码")
@RestController
@RequestMapping("/api/veriCodes")
public class VeriCodeController extends BaseController {
    private static Logger logger = Logger.getLogger(VeriCodeController.class);

    @Autowired
    private SmsService smsService;

    @Autowired
    private VeriCodeRepository veriCodeRepository;

    @Value("${com.alibaba.dayu.url.sandbox}")
    private String DAYU_URL_SANDBOX;

    @Value("${com.alibaba.dayu.url.real}")
    private String DAYU_URL_REAL;

    @Value("${com.alibaba.dayu.app-key}")
    private String DAYU_APP_KEY;

    @Value("${com.alibaba.dayu.app-secret}")
    private String DAYU_APP_SECRET;

    @Value("${com.alibaba.dayu.sms-type}")
    private String DAYU_SMS_TYPE;

    @Value("${com.alibaba.dayu.sms-free-sign-name}")
    private String DAYU_SMS_FREE_SIGN_NAME;

    @Value("${com.alibaba.dayu.sms-template-code}")
    private String DAYU_SMS_TEMPLATE_CODE;

    @ApiOperation(value = "获取验证码", notes = "验证码有效时间是30分钟，验证码在失效前5分钟就会重新生成一个返回，给用户通过验证码修改密码足够的时间")
    @RequestMapping(value = "/getRegisterCode", method = RequestMethod.GET)
    public ResultMsg getVeriCode(@RequestParam String phone) {

        var veriCode = veriCodeRepository.findByPhone(phone);
        if (null == veriCode) {
            veriCode = new VeriCode();
            veriCode.setPhone(phone);
            veriCode.setCode(RandomUtils.nextInt(100000, 999999));
            veriCode.setCreateTime(System.currentTimeMillis());
            veriCode.setExpires(30 * 60);
            veriCodeRepository.save(veriCode);
        } else {
            if (System.currentTimeMillis() > (veriCode.getCreateTime() + veriCode.getExpires() - 60 * 5)) {
                veriCode.setCode(RandomUtils.nextInt(100000, 999999));
                veriCode.setCreateTime(System.currentTimeMillis());
                veriCode.setExpires(30 * 60);
                veriCodeRepository.save(veriCode);
            }
        }
        var client = new DefaultTaobaoClient(
                DAYU_URL_REAL, DAYU_APP_KEY, DAYU_APP_SECRET);
        var req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType(DAYU_SMS_TYPE);
        req.setSmsFreeSignName(DAYU_SMS_FREE_SIGN_NAME);
        req.setSmsParamString("{veriCode:'" + veriCode.getCode() + "'}");
        req.setRecNum(phone);
        req.setSmsTemplateCode(DAYU_SMS_TEMPLATE_CODE);
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
            BizResult bizResult = rsp.getResult();
            if (null != bizResult && bizResult.getSuccess()) {
                return new ResultMsg(200, "验证码已发送");
            } else {
                logger.error("请确认阿里大于账号还有余额");
                return ResultMsg.fail(500, "验证码发送失败，请稍后重试");
            }
        } catch (ApiException e) {
            e.printStackTrace();
            return ResultMsg.fail(500, e.getErrMsg());
        }
    }

    @ApiOperation(value = "校验验证码")
    @RequestMapping(value = "/checkVeriCode", method = RequestMethod.GET)
    public ResultMsg checkVeriCode(@RequestParam String phone, @RequestParam Integer code) {
        try {
            var veriCode = veriCodeRepository.findByPhoneAndCode(phone, code);
            if (null != veriCode) {
                return ResultMsg.success(200, "验证码正确");
            } else {
                return ResultMsg.fail(404, "验证码错误");
            }
        } catch (Exception e) {
            return ResultMsg.fail(500, "验证码错误", e.getMessage());
        }
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping("/sms/{phone}")
    public ResultMsg getVeriCodeByPhone(@PathVariable String phone) {
        if (smsService.sendIdentifyCode(phone)) {
            return ResultMsg.success();
        } else {
            return ResultMsg.fail(ResultCode.FAIL);
        }
    }
}
