/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.nosql.couchdb.controller;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.nosql.couchdb.domain.Response;
import cn.herodotus.engine.nosql.couchdb.dto.Database;
import cn.herodotus.engine.nosql.couchdb.service.DatabaseService;
import cn.herodotus.engine.rest.core.controller.Controller;
import cn.herodotus.engine.rest.core.definition.dto.Pager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>Description: CouchDB 数据库管理接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/8/27 16:59
 */
@RestController
@RequestMapping("/couchdb/database")
@Tags({
        @Tag(name = "Nosql 管理接口"),
        @Tag(name = "CouchDB 管理接口"),
        @Tag(name = "CouchDB Database 接口"),
})
public class DatabaseController implements Controller {

    private final DatabaseService databaseService;

    @Autowired
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Operation(summary = "创建数据库", description = "创建 CouchDB 数据库名称",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "Result", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "database", required = true, description = "CouchDB 数据库名称", schema = @Schema(implementation = Database.class))
    })
    @PostMapping
    public Result<Response> create(@Validated @RequestBody Database database) {
        Response response =  databaseService.create(database.getName());
        return result(response);
    }
}
