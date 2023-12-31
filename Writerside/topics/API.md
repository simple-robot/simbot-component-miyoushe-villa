# API

针对一些 API 的描述与介绍。

## 通用特征

在
<tooltip term="大别野组件">大别野组件</tooltip>
中，所有针对大别野 HTTP API 进行封装的 API 类型，都具有一定的 
<control>通用特征</control> 
。


### 统一实现

所有的大别野 API 的实现类型（例如 `GetRoomApi`）都会实现接口 `MiyousheVillaApi` 。

`MiyousheVillaApi` 定义了大别野 API 所需的部分属性或能力，
并且借助这些信息也应当支持由 `Ktor` 以外的 HTTP 客户端实现来使用它们。

> 换句话说就是 `MiyousheVillaApi` 约束其实现类型对外提供包括请求路径、请求方法(`method`)、
> 请求体在内的基本信息。

### 不可变

所有大别野 API 的实现都应当是**不可变**的。这说明它们可以共享、并发安全、互不干扰。

### 工厂构造

所有大别野 API 的实现类型都应当私有化其构造，并以公开的**工厂函数**取而代之。

例如 `GetRoomApi`，构建其实例的方式是使用 `GetRoomApi.create(...)`，而不是直接构造。

> 绝大多数实现类型的工厂函数都是以 `create` 或类似（例如以它为前缀的 `createByXxx`）命名。
> 存在部分特殊类型会使用其他命名，例如当参数过多时会以提供 `builder` 代替 `create`。