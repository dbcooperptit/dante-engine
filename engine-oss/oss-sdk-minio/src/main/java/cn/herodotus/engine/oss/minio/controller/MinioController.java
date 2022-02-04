/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.oss.minio.controller;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.oss.minio.core.MinioTemplate;
import cn.herodotus.engine.oss.minio.domain.MinioItem;
import cn.herodotus.engine.oss.minio.domain.MinioObject;
import cn.herodotus.engine.oss.minio.domain.ObjectInfo;
import io.minio.messages.Bucket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: TODO </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/8 18:26
 */
@RestController
@RequestMapping("/oss/minio")
@Tags({@Tag(name = "Minio 对象存储接口"), @Tag(name = "对象存储接口"), @Tag(name = "外部应用集成接口")})
public class MinioController {

    @Autowired
    private MinioTemplate minioTemplate;

    @Operation(summary = "创建Bucket", description = "创建Bucket",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "Bucket详情", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "bucketName", required = true, in = ParameterIn.PATH, description = "userId"),
    })
    @PostMapping("/bucket/{bucketName}")
    public Result<Bucket> createBucker(@PathVariable String bucketName){
        minioTemplate.createBucket(bucketName);
        Bucket bucket = minioTemplate.getBucket(bucketName).get();
        if (ObjectUtils.isNotEmpty(bucket)) {
            return Result.success("创建成功!", bucket);
        } else {
            return Result.failure("创建失败！");
        }
    }

    @Operation(summary = "获取全部Bucket", description = "获取全部Bucket",
            responses = {@ApiResponse(description = "Bucket列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))})
    @GetMapping("/bucket")
    public Result<List<Bucket>> getBuckets() {
        List<Bucket> buckets = minioTemplate.getAllBuckets();
        if (ObjectUtils.isNotEmpty(buckets)) {
            if (CollectionUtils.isNotEmpty(buckets)) {
                return Result.success("查询数据成功！", buckets);
            } else {
                return Result.empty("未查询到数据！");
            }
        } else {
            return Result.failure("查询数据失败！");
        }
    }

    @Operation(summary = "删除Bucket", description = "根据名称获取bucket",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "操作消息", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "bucketName", required = true, in = ParameterIn.PATH, description = "Bucket名称"),
    })
    @GetMapping("/bucket/{bucketName}")
    public Result<Bucket> getBucket(@PathVariable String bucketName) {
        Bucket bucket = minioTemplate.getBucket(bucketName).get();
        if (ObjectUtils.isNotEmpty(bucket)) {
            return Result.success("创建成功!", bucket);
        } else {
            return Result.failure("创建失败！");
        }
    }

    @Operation(summary = "删除Bucket", description = "删除bucket",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "操作消息", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "bucketName", required = true, in = ParameterIn.PATH, description = "Bucket名称"),
    })
    @DeleteMapping("/bucket/{bucketName}")
    public Result<String> deleteBucket(@PathVariable String bucketName) {
        minioTemplate.removeBucket(bucketName);
        return Result.success("删除成功");
    }

    @Operation(summary = "文件上传", description = "存入对象到bucket并设置对象名称",
            responses = {@ApiResponse(description = "单位列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))})
    @Parameters({
            @Parameter(name = "multipartFile", required = true, description = "multipartFile对象", schema = @Schema(implementation = MultipartFile.class)),
            @Parameter(name = "bucketName", required = true, in = ParameterIn.PATH, description = "Bucket名称")
    })
    @PostMapping("/object/{bucketName}")
    public Result<MinioObject> putObject(@RequestBody MultipartFile multipartFile, @PathVariable String bucketName) {
        String name = multipartFile.getOriginalFilename();
        return this.putObject(multipartFile, bucketName, name);

    }

    @Operation(summary = "文件上传", description = "存入对象到bucket并设置对象名称",
            responses = {@ApiResponse(description = "单位列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))})
    @Parameters({
            @Parameter(name = "multipartFile", required = true, description = "multipartFile对象", schema = @Schema(implementation = MultipartFile.class)),
            @Parameter(name = "bucketName", required = true, in = ParameterIn.PATH, description = "Bucket名称"),
            @Parameter(name = "objectName", required = true, in = ParameterIn.PATH, description = "对象名称"),
    })
    @PostMapping("/object/{bucketName}/{objectName}")
    public Result<MinioObject> putObject(@RequestBody MultipartFile multipartFile, @PathVariable String bucketName, @PathVariable String objectName) {
        try {
            minioTemplate.putObject(bucketName, objectName, multipartFile.getInputStream(), multipartFile.getSize(), multipartFile.getContentType());
            MinioObject minioObject = new MinioObject(minioTemplate.getObjectInfo(bucketName, objectName));
            return Result.success("上传成功", minioObject);
        } catch (IOException e) {
            return Result.failure("上传失败，MultipartFile IO 错误！");
        }
    }


    @Operation(summary = "获取对象", description = "根据bucket名称和对象名称过滤所有对象",
            responses = {@ApiResponse(description = "单位列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))})
    @Parameters({
            @Parameter(name = "bucketName", required = true, in = ParameterIn.PATH, description = "Bucket名称"),
            @Parameter(name = "objectName", required = true, in = ParameterIn.PATH, description = "对象名称"),
    })
    @GetMapping("/object/{bucketName}/{objectName}")
    public  Result<List<MinioItem>>  filterObject(@PathVariable String bucketName, @PathVariable String objectName)  {
        List<MinioItem> minioItems = minioTemplate.getAllObjectsByPrefix(bucketName, objectName, true);
        if (ObjectUtils.isNotEmpty(minioItems)) {
            if (CollectionUtils.isNotEmpty(minioItems)) {
                return Result.success("查询数据成功！", minioItems);
            } else {
                return Result.empty("未查询到数据！");
            }
        } else {
            return Result.failure("查询数据失败！");
        }
    }

    @Operation(summary = "获取对象", description = "根据名称获取bucket下的对象并设置外链的过期时间",
            responses = {@ApiResponse(description = "单位列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectInfo.class)))})
    @Parameters({
            @Parameter(name = "bucketName", required = true, in = ParameterIn.PATH, description = "Bucket名称"),
            @Parameter(name = "objectName", required = true, in = ParameterIn.PATH, description = "对象名称"),
            @Parameter(name = "expires", required = true, in = ParameterIn.PATH, description = "过期时间，Duration表达式"),
    })
    @GetMapping("/object/{bucketName}/{objectName}/{expires}")
    public Result<ObjectInfo> getObject(@PathVariable String bucketName, @PathVariable String objectName, @PathVariable String expires) throws Exception {

        Duration expireDuration = Duration.parse(expires);
        if (expireDuration != Duration.ZERO) {
            String url = minioTemplate.getObjectURL(bucketName, objectName,expireDuration);
            if (StringUtils.isNotBlank(url)) {
                ObjectInfo objectInfo = new ObjectInfo();
                objectInfo.setObjectName(objectName);
                objectInfo.setBucketName(bucketName);
                objectInfo.setUrl(url);
                objectInfo.setExpires(expireDuration.toString());

                return Result.success("获取成功！",objectInfo);
            } else {
                return Result.failure("获取失败！");
            }
        } else {
            return Result.failure("Expires 参数格式错误!");
        }
    }

    @Operation(summary = "删除对象", description = "根据Bucket名字和对象名称删除对象",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "操作消息", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "bucketName", required = true, in = ParameterIn.PATH, description = "Bucket名称"),
            @Parameter(name = "objectName", required = true, in = ParameterIn.PATH, description = "对象名称")
    })
    @DeleteMapping("/object/{bucketName}/{objectName}/")
    public Result<String> deleteObject(@PathVariable String bucketName, @PathVariable String objectName) {
        minioTemplate.removeObject(bucketName, objectName);
        return Result.success("删除成功");
    }
}
