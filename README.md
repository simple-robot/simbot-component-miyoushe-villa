# Simple Robot 米游社组件

此为 [Simple Robot v3][simbot3] （以下简称为 `simbot3` ）
下基于simbot标准API对 [米游社 API](https://webstatic.mihoyo.com/vila/bot/doc/) 的组件支持。

[simbot3]: https://github.com/simple-robot/simpler-robot

## 文档

了解**simbot3**: [simbot3官网](https://simbot.forte.love)

目标：

- 全部API的定义于实现
    - 所有定义中，无符号类型均会直接使用 Kotlin unsigned number
- 基于 WS 的事件订阅
- 实现 simbot3 API

API 进度：

- [x] WebSocket API
- [x] 鉴权 API
- [x] 大别野 API
- [x] 用户 API
- [x] 消息 API
- [x] 房间 API
- [x] 身份组 API
- [x] 表态表情 API
- [x] 审核 API
- [ ] 图片 API

> [!warning]
> ⚠️ 施工中

## License

`simbot-component-miyoushe` 使用 `LGPLv3` 许可证开源。

```
This program is free software: you can redistribute it and/or modify it under the terms of 
the GNU Lesser General Public License as published by the Free Software Foundation, either 
version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this 
program. If not, see <https://www.gnu.org/licenses/>.

