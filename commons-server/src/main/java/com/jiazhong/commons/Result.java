package com.jiazhong.commons;

import lombok.Getter;

@Getter
public class Result {
    private boolean isSuccess;//true表示成功，false表示失败
    private int stateCode;//状态码
    private String message;//消息
    private Object data;//附加数据

    private Result(boolean isSuccess, int stateCode, String message, Object data) {
        this.isSuccess = isSuccess;
        this.stateCode = stateCode;
        this.message = message;
        this.data = data;
    }





    /**
     * 默认成功
     * @return
     */
    public static Result success(){
        return new Result(true,200,null,null);
    }

    /**
     * 带有消息的成功
     * @return
     */
    public static Result success(String message){
        return new Result(true,200,message,null);
    }

    /**
     * 带有消息和附加数据的成功
     * @return
     */
    public static Result success(String message,Object data){
        return new Result(true,200,message,data);
    }

    /**
     * 带有消息和附加数据的失败
     * @return
     */
    public static Result fail(int stateCode,String message,Object data){
        return new Result(false,stateCode,message,data);
    }

    /**
     * 带有消息失败
     * @return
     */
    public static Result fail(int stateCode,String message){
        return new Result(false,stateCode,message,null);
    }


    /**
     * 默认失败
     * @return
     */
    public static Result fail(int stateCode){
        return new Result(false,stateCode,null,null);
    }
}
