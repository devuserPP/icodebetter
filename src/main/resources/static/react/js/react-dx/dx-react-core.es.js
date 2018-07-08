/**
 * Bundle of @devexpress/dx-react-core
 * Generated: 2018-03-20
 * Version: 1.1.1
 * License: https://js.devexpress.com/Licensing
 */

var DXReactCore=function(){
//import { Children, Component, Fragment, PureComponent, createElement } from 'react';
//	import { any, arrayOf, bool, func, node, object, oneOfType, shape, string } from 'prop-types';
var any=PropTypes.any, arrayOf=PropTypes.arrayOf, bool=PropTypes.bool, func=PropTypes.func, node=PropTypes.node
	, object=PropTypes.object, oneOfType=PropTypes.oneOfType, shape=PropTypes.shape, string=PropTypes.string;// } from 'prop-types';
//import { EventEmitter, PluginHost, shallowEqual } from '@devexpress/dx-core';
var EventEmitter=DXCore.EventEmitter, PluginHost=DXCore.PluginHost, shallowEqual=DXCore.shallowEqual;
//import { findDOMNode, unstable_batchedUpdates } from 'react-dom';
var unstable_batchedUpdates = ReactDOM.unstable_batchedUpdates;
var PLUGIN_HOST_CONTEXT = 'dxcore_pluginHost_context';
var POSITION_CONTEXT = 'dxcore_position_context';
var TEMPLATE_HOST_CONTEXT = 'dxcore_templateHost_context';

var RERENDER_TEMPLATE_EVENT = Symbol('rerenderTemplate');
var UPDATE_CONNECTION_EVENT = Symbol('updateConnection');

var asyncGenerator = function () {
  function AwaitValue(value) {
    this.value = value;
  }

  function AsyncGenerator(gen) {
    var front, back;

    function send(key, arg) {
      return new Promise(function (resolve, reject) {
        var request = {
          key: key,
          arg: arg,
          resolve: resolve,
          reject: reject,
          next: null
        };

        if (back) {
          back = back.next = request;
        } else {
          front = back = request;
          resume(key, arg);
        }
      });
    }

    function resume(key, arg) {
      try {
        var result = gen[key](arg);
        var value = result.value;

        if (value instanceof AwaitValue) {
          Promise.resolve(value.value).then(function (arg) {
            resume("next", arg);
          }, function (arg) {
            resume("throw", arg);
          });
        } else {
          settle(result.done ? "return" : "normal", result.value);
        }
      } catch (err) {
        settle("throw", err);
      }
    }

    function settle(type, value) {
      switch (type) {
        case "return":
          front.resolve({
            value: value,
            done: true
          });
          break;

        case "throw":
          front.reject(value);
          break;

        default:
          front.resolve({
            value: value,
            done: false
          });
          break;
      }

      front = front.next;

      if (front) {
        resume(front.key, front.arg);
      } else {
        back = null;
      }
    }

    this._invoke = send;

    if (typeof gen.return !== "function") {
      this.return = undefined;
    }
  }

  if (typeof Symbol === "function" && Symbol.asyncIterator) {
    AsyncGenerator.prototype[Symbol.asyncIterator] = function () {
      return this;
    };
  }

  AsyncGenerator.prototype.next = function (arg) {
    return this._invoke("next", arg);
  };

  AsyncGenerator.prototype.throw = function (arg) {
    return this._invoke("throw", arg);
  };

  AsyncGenerator.prototype.return = function (arg) {
    return this._invoke("return", arg);
  };

  return {
    wrap: function (fn) {
      return function () {
        return new AsyncGenerator(fn.apply(this, arguments));
      };
    },
    await: function (value) {
      return new AwaitValue(value);
    }
  };
}();





var classCallCheck = function (instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
};

var createClass = function () {
  function defineProperties(target, props) {
    for (var i = 0; i < props.length; i++) {
      var descriptor = props[i];
      descriptor.enumerable = descriptor.enumerable || false;
      descriptor.configurable = true;
      if ("value" in descriptor) descriptor.writable = true;
      Object.defineProperty(target, descriptor.key, descriptor);
    }
  }

  return function (Constructor, protoProps, staticProps) {
    if (protoProps) defineProperties(Constructor.prototype, protoProps);
    if (staticProps) defineProperties(Constructor, staticProps);
    return Constructor;
  };
}();





var defineProperty = function (obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, {
      value: value,
      enumerable: true,
      configurable: true,
      writable: true
    });
  } else {
    obj[key] = value;
  }

  return obj;
};

var _extends = Object.assign || function (target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i];

    for (var key in source) {
      if (Object.prototype.hasOwnProperty.call(source, key)) {
        target[key] = source[key];
      }
    }
  }

  return target;
};



var inherits = function (subClass, superClass) {
  if (typeof superClass !== "function" && superClass !== null) {
    throw new TypeError("Super expression must either be null or a function, not " + typeof superClass);
  }

  subClass.prototype = Object.create(superClass && superClass.prototype, {
    constructor: {
      value: subClass,
      enumerable: false,
      writable: true,
      configurable: true
    }
  });
  if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass;
};











var possibleConstructorReturn = function (self, call) {
  if (!self) {
    throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
  }

  return call && (typeof call === "object" || typeof call === "function") ? call : self;
};





var slicedToArray = function () {
  function sliceIterator(arr, i) {
    var _arr = [];
    var _n = true;
    var _d = false;
    var _e = undefined;

    try {
      for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) {
        _arr.push(_s.value);

        if (i && _arr.length === i) break;
      }
    } catch (err) {
      _d = true;
      _e = err;
    } finally {
      try {
        if (!_n && _i["return"]) _i["return"]();
      } finally {
        if (_d) throw _e;
      }
    }

    return _arr;
  }

  return function (arr, i) {
    if (Array.isArray(arr)) {
      return arr;
    } else if (Symbol.iterator in Object(arr)) {
      return sliceIterator(arr, i);
    } else {
      throw new TypeError("Invalid attempt to destructure non-iterable instance");
    }
  };
}();













var toConsumableArray = function (arr) {
  if (Array.isArray(arr)) {
    for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) arr2[i] = arr[i];

    return arr2;
  } else {
    return Array.from(arr);
  }
};

var PluginIndexer = function PluginIndexer(_ref, _ref2) {
  var children = _ref.children;
  var position = _ref2[POSITION_CONTEXT];
  return _(
    React.Fragment,
    null,
    React.Children.map(children, function (child, index) {
      if (!child || !child.type) return child;

      var childPosition = function childPosition() {
        var calculatedPosition = position && position() || [];
        return [].concat(toConsumableArray(calculatedPosition), [index]);
      };

      return _(
        PluginIndexerContext,
        { position: childPosition },
        child
      );
    })
  );
};

PluginIndexer.propTypes = {
  children: node
};

PluginIndexer.defaultProps = {
  children: undefined
};

PluginIndexer.contextTypes = defineProperty({}, POSITION_CONTEXT, func);

var PluginIndexerContext = function (_React$Component) {
  inherits(PluginIndexerContext, _React$Component);

  function PluginIndexerContext() {
    classCallCheck(this, PluginIndexerContext);
    return possibleConstructorReturn(this, (PluginIndexerContext.__proto__ || Object.getPrototypeOf(PluginIndexerContext)).apply(this, arguments));
  }

  createClass(PluginIndexerContext, [{
    key: 'getChildContext',
    value: function getChildContext() {
      return defineProperty({}, POSITION_CONTEXT, this.props.position);
    }
  }, {
    key: 'render',
    value: function render() {
      return this.props.children;
    }
  }]);
  return PluginIndexerContext;
}(React.Component);

PluginIndexerContext.propTypes = {
  position: func.isRequired,
  children: node.isRequired
};

PluginIndexerContext.childContextTypes = defineProperty({}, POSITION_CONTEXT, func);

var _Template$contextType;

var globalTemplateId = 0;
var Template = function (_React$PureComponent) {
  inherits(Template, _React$PureComponent);

  function Template(props, context) {
    classCallCheck(this, Template);

    var _this = possibleConstructorReturn(this, (Template.__proto__ || Object.getPrototypeOf(Template)).call(this, props, context));

    globalTemplateId += 1;
    _this.id = globalTemplateId;
    return _this;
  }

  createClass(Template, [{
    key: 'componentWillMount',
    value: function componentWillMount() {
      var _this2 = this;

      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];
      var name = this.props.name;


      this.plugin = defineProperty({
        position: function position() {
          return _this2.context[POSITION_CONTEXT]();
        }
      }, name + 'Template', {
        id: this.id,
        predicate: function predicate(params) {
          return _this2.props.predicate ? _this2.props.predicate(params) : true;
        },
        children: function children() {
          return _this2.props.children;
        }
      });
      pluginHost.registerPlugin(this.plugin);
    }
  }, {
    key: 'componentDidUpdate',
    value: function componentDidUpdate() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      pluginHost.broadcast(RERENDER_TEMPLATE_EVENT, this.id);
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      pluginHost.unregisterPlugin(this.plugin);
    }
  }, {
    key: 'render',
    value: function render() {
      return null;
    }
  }]);
  return Template;
}(React.PureComponent);

Template.propTypes = {
  position: func,
  name: string.isRequired,
  predicate: func,
  children: oneOfType([func, node])
};

Template.defaultProps = {
  predicate: undefined,
  children: undefined,
  position: undefined
};

Template.contextTypes = (_Template$contextType = {}, defineProperty(_Template$contextType, PLUGIN_HOST_CONTEXT, object.isRequired), defineProperty(_Template$contextType, POSITION_CONTEXT, func.isRequired), _Template$contextType);

var _TemplatePlaceholder$;

var TemplatePlaceholder = function (_React$Component) {
  inherits(TemplatePlaceholder, _React$Component);

  function TemplatePlaceholder(props, context) {
    classCallCheck(this, TemplatePlaceholder);

    var _this = possibleConstructorReturn(this, (TemplatePlaceholder.__proto__ || Object.getPrototypeOf(TemplatePlaceholder)).call(this, props, context));

    _this.subscription = defineProperty({}, RERENDER_TEMPLATE_EVENT, function (id) {
      if (_this.template && _this.template.id === id) {
        _this.forceUpdate();
      }
    });
    return _this;
  }

  createClass(TemplatePlaceholder, [{
    key: 'getChildContext',
    value: function getChildContext() {
      var _this2 = this;

      return defineProperty({}, TEMPLATE_HOST_CONTEXT, {
        templates: function templates() {
          return _this2.restTemplates;
        },
        params: function params() {
          return _this2.params;
        }
      });
    }
  }, {
    key: 'componentWillMount',
    value: function componentWillMount() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      pluginHost.registerSubscription(this.subscription);
    }
  }, {
    key: 'shouldComponentUpdate',
    value: function shouldComponentUpdate(nextProps) {
      var _getRenderingData = this.getRenderingData(nextProps),
          params = _getRenderingData.params;

      return !shallowEqual(params, this.params) || this.props.children !== nextProps.children;
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      pluginHost.unregisterSubscription(this.subscription);
    }
  }, {
    key: 'getRenderingData',
    value: function getRenderingData(props) {
      var name = props.name,
          params = props.params;

      if (name) {
        var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

        return {
          params: params,
          templates: pluginHost.collect(name + 'Template').filter(function (template) {
            return template.predicate(params);
          }).reverse()
        };
      }
      var templateHost = this.context[TEMPLATE_HOST_CONTEXT];

      return {
        params: params || templateHost.params(),
        templates: templateHost.templates()
      };
    }
  }, {
    key: 'render',
    value: function render() {
      var _getRenderingData2 = this.getRenderingData(this.props),
          params = _getRenderingData2.params,
          templates = _getRenderingData2.templates;

      this.params = params;

      var _templates = slicedToArray(templates, 1);

      this.template = _templates[0];

      this.restTemplates = templates.slice(1);

      var content = null;
      if (this.template) {
        var templateContent = this.template.children;


        content = templateContent() || null;
        if (content && typeof content === 'function') {
          content = content(params);
        }
      }

      var templatePlaceholder = this.props.children;

      return templatePlaceholder ? templatePlaceholder(content) : content;
    }
  }]);
  return TemplatePlaceholder;
}(React.Component);

TemplatePlaceholder.propTypes = {
  name: string, // eslint-disable-line react/no-unused-prop-types
  params: object, // eslint-disable-line react/no-unused-prop-types
  children: func
};

TemplatePlaceholder.defaultProps = {
  name: undefined,
  params: undefined,
  children: undefined
};

TemplatePlaceholder.contextTypes = (_TemplatePlaceholder$ = {}, defineProperty(_TemplatePlaceholder$, TEMPLATE_HOST_CONTEXT, object), defineProperty(_TemplatePlaceholder$, PLUGIN_HOST_CONTEXT, object.isRequired), _TemplatePlaceholder$);

TemplatePlaceholder.childContextTypes = defineProperty({}, TEMPLATE_HOST_CONTEXT, object.isRequired);

var PluginHost$1 = function (_React$PureComponent) {
  inherits(PluginHost$$1, _React$PureComponent);

  function PluginHost$$1(props) {
    classCallCheck(this, PluginHost$$1);

    var _this = possibleConstructorReturn(this, (PluginHost$$1.__proto__ || Object.getPrototypeOf(PluginHost$$1)).call(this, props));

    _this.host = new PluginHost();
    return _this;
  }

  createClass(PluginHost$$1, [{
    key: 'getChildContext',
    value: function getChildContext() {
      return defineProperty({}, PLUGIN_HOST_CONTEXT, this.host);
    }
  }, {
    key: 'render',
    value: function render() {
      var children = this.props.children;


      return _(
        React.Fragment,
        null,
        _(
          PluginIndexer,
          null,
          _(Template, { name: 'root' }),
          children
        ),
        _(TemplatePlaceholder, { name: 'root' })
      );
    }
  }]);
  return PluginHost$$1;
}(React.PureComponent);

PluginHost$1.propTypes = {
  children: node
};

PluginHost$1.defaultProps = {
  children: undefined
};

PluginHost$1.childContextTypes = defineProperty({}, PLUGIN_HOST_CONTEXT, object.isRequired);

var _Plugin$contextTypes;

var Plugin = function (_React$PureComponent) {
  inherits(Plugin, _React$PureComponent);

  function Plugin() {
    classCallCheck(this, Plugin);
    return possibleConstructorReturn(this, (Plugin.__proto__ || Object.getPrototypeOf(Plugin)).apply(this, arguments));
  }

  createClass(Plugin, [{
    key: 'componentWillMount',
    value: function componentWillMount() {
      var _context = this.context,
          pluginHost = _context[PLUGIN_HOST_CONTEXT],
          position = _context[POSITION_CONTEXT];
      var _props = this.props,
          name = _props.name,
          dependencies = _props.dependencies;

      this.plugin = {
        position: position,
        name: name,
        dependencies: dependencies,
        container: true
      };
      pluginHost.registerPlugin(this.plugin);
    }
  }, {
    key: 'componentWillUpdate',
    value: function componentWillUpdate() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      pluginHost.ensureDependencies();
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      pluginHost.unregisterPlugin(this.plugin);
    }
  }, {
    key: 'render',
    value: function render() {
      var children = this.props.children;

      return _(
        PluginIndexer,
        null,
        children
      );
    }
  }]);
  return Plugin;
}(React.PureComponent);

Plugin.propTypes = {
  children: node.isRequired,
  name: string,
  dependencies: arrayOf(shape({
    name: string,
    optional: bool
  }))
};

Plugin.defaultProps = {
  name: '',
  dependencies: []
};

Plugin.contextTypes = (_Plugin$contextTypes = {}, defineProperty(_Plugin$contextTypes, PLUGIN_HOST_CONTEXT, object.isRequired), defineProperty(_Plugin$contextTypes, POSITION_CONTEXT, func.isRequired), _Plugin$contextTypes);

var getAvailableGetters = function getAvailableGetters(pluginHost) {
  var getGetterValue = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : function (getterName) {
    return pluginHost.get(getterName + 'Getter');
  };

  var trackedDependencies = {};
  var getters = pluginHost.knownKeys('Getter').reduce(function (acc, getterName) {
    Object.defineProperty(acc, getterName, {
      get: function get$$1() {
        var result = getGetterValue(getterName);
        trackedDependencies[getterName] = result;
        return result;
      }
    });
    return acc;
  }, {});

  return { getters: getters, trackedDependencies: trackedDependencies };
};

var isTrackedDependenciesChanged = function isTrackedDependenciesChanged(pluginHost, prevTrackedDependencies) {
  var getGetterValue = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : function (getterName) {
    return pluginHost.get(getterName + 'Getter');
  };

  var trackedDependencies = Object.keys(prevTrackedDependencies).reduce(function (acc, getterName) {
    return Object.assign(acc, defineProperty({}, getterName, getGetterValue(getterName)));
  }, {});

  return !shallowEqual(prevTrackedDependencies, trackedDependencies);
};

var getAvailableActions = function getAvailableActions(pluginHost) {
  var getAction = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : function (actionName) {
    return pluginHost.collect(actionName + 'Action').slice().reverse()[0];
  };
  return pluginHost.knownKeys('Action').reduce(function (acc, actionName) {
    Object.defineProperty(acc, actionName, {
      get: function get$$1() {
        return getAction(actionName);
      }
    });
    return acc;
  }, {});
};

var _Action$contextTypes;

var Action = function (_React$PureComponent) {
  inherits(Action, _React$PureComponent);

  function Action() {
    classCallCheck(this, Action);
    return possibleConstructorReturn(this, (Action.__proto__ || Object.getPrototypeOf(Action)).apply(this, arguments));
  }

  createClass(Action, [{
    key: 'componentWillMount',
    value: function componentWillMount() {
      var _this2 = this;

      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];
      var name = this.props.name;


      this.plugin = defineProperty({
        position: function position() {
          return _this2.context[POSITION_CONTEXT]();
        }
      }, name + 'Action', function undefined(params) {
        var action = _this2.props.action;

        var _getAvailableGetters = getAvailableGetters(pluginHost, function (getterName) {
          return pluginHost.get(getterName + 'Getter', _this2.plugin);
        }),
            getters = _getAvailableGetters.getters;

        var nextParams = params;
        var actions = getAvailableActions(pluginHost, function (actionName) {
          return actionName === name ? function (newParams) {
            nextParams = newParams;
          } : pluginHost.collect(actionName + 'Action', _this2.plugin).slice().reverse()[0];
        });
        action(params, getters, actions);
        var nextAction = pluginHost.collect(name + 'Action', _this2.plugin).slice().reverse()[0];
        if (nextAction) {
          nextAction(nextParams);
        }
      });

      pluginHost.registerPlugin(this.plugin);
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];


      pluginHost.unregisterPlugin(this.plugin);
    }
  }, {
    key: 'render',
    value: function render() {
      return null;
    }
  }]);
  return Action;
}(React.PureComponent);

Action.propTypes = {
  name: string.isRequired,
  action: func.isRequired
};

Action.contextTypes = (_Action$contextTypes = {}, defineProperty(_Action$contextTypes, PLUGIN_HOST_CONTEXT, object.isRequired), defineProperty(_Action$contextTypes, POSITION_CONTEXT, func.isRequired), _Action$contextTypes);

var _Getter$contextTypes;

var Getter = function (_React$PureComponent) {
  inherits(Getter, _React$PureComponent);

  function Getter() {
    classCallCheck(this, Getter);
    return possibleConstructorReturn(this, (Getter.__proto__ || Object.getPrototypeOf(Getter)).apply(this, arguments));
  }

  createClass(Getter, [{
    key: 'componentWillMount',
    value: function componentWillMount() {
      var _this2 = this;

      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];
      var name = this.props.name;


      var lastComputed = void 0;
      var lastTrackedDependencies = {};
      var lastResult = void 0;

      this.plugin = defineProperty({
        position: function position() {
          return _this2.context[POSITION_CONTEXT]();
        }
      }, name + 'Getter', function (_undefined) {
        function undefined(_x) {
          return _undefined.apply(this, arguments);
        }

        undefined.toString = function () {
          return _undefined.toString();
        };

        return undefined;
      }(function (original) {
        var _props = _this2.props,
            value = _props.value,
            computed = _props.computed;

        if (value !== undefined) return value;

        var getGetterValue = function getGetterValue(getterName) {
          return getterName === name ? original : pluginHost.get(getterName + 'Getter', _this2.plugin);
        };

        if (computed === lastComputed && !isTrackedDependenciesChanged(pluginHost, lastTrackedDependencies, getGetterValue)) {
          return lastResult;
        }

        var _getAvailableGetters = getAvailableGetters(pluginHost, getGetterValue),
            getters = _getAvailableGetters.getters,
            trackedDependencies = _getAvailableGetters.trackedDependencies;

        var actions = getAvailableActions(pluginHost);

        lastComputed = computed;
        lastTrackedDependencies = trackedDependencies;
        lastResult = computed(getters, actions);
        return lastResult;
      }));

      pluginHost.registerPlugin(this.plugin);
    }
  }, {
    key: 'componentDidUpdate',
    value: function componentDidUpdate() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];


      pluginHost.broadcast(UPDATE_CONNECTION_EVENT);
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];


      pluginHost.unregisterPlugin(this.plugin);
    }
  }, {
    key: 'render',
    value: function render() {
      return null;
    }
  }]);
  return Getter;
}(React.PureComponent);

Getter.propTypes = {
  name: string.isRequired,
  value: any,
  computed: func
};

Getter.defaultProps = {
  value: undefined,
  computed: null
};

Getter.contextTypes = (_Getter$contextTypes = {}, defineProperty(_Getter$contextTypes, PLUGIN_HOST_CONTEXT, object.isRequired), defineProperty(_Getter$contextTypes, POSITION_CONTEXT, func.isRequired), _Getter$contextTypes);

var TemplateConnector = function (_React$Component) {
  inherits(TemplateConnector, _React$Component);

  function TemplateConnector(props, context) {
    classCallCheck(this, TemplateConnector);

    var _this = possibleConstructorReturn(this, (TemplateConnector.__proto__ || Object.getPrototypeOf(TemplateConnector)).call(this, props, context));

    _this.trackedDependencies = {};
    _this.subscription = defineProperty({}, UPDATE_CONNECTION_EVENT, function () {
      return _this.updateConnection();
    });
    return _this;
  }

  createClass(TemplateConnector, [{
    key: 'componentWillMount',
    value: function componentWillMount() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      pluginHost.registerSubscription(this.subscription);
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      pluginHost.unregisterSubscription(this.subscription);
    }
  }, {
    key: 'updateConnection',
    value: function updateConnection() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];


      if (isTrackedDependenciesChanged(pluginHost, this.trackedDependencies)) {
        this.forceUpdate();
      }
    }
  }, {
    key: 'render',
    value: function render() {
      var pluginHost = this.context[PLUGIN_HOST_CONTEXT];

      var _getAvailableGetters = getAvailableGetters(pluginHost),
          getters = _getAvailableGetters.getters,
          trackedDependencies = _getAvailableGetters.trackedDependencies;

      this.trackedDependencies = trackedDependencies;
      var actions = getAvailableActions(pluginHost);

      return this.props.children(getters, actions);
    }
  }]);
  return TemplateConnector;
}(React.Component);

TemplateConnector.propTypes = {
  children: func.isRequired
};

TemplateConnector.contextTypes = defineProperty({}, PLUGIN_HOST_CONTEXT, object.isRequired);

var TIMEOUT = 180;

var TouchStrategy = function () {
  function TouchStrategy(delegate) {
    classCallCheck(this, TouchStrategy);

    this.delegate = delegate;
    this.touchStartTimeout = null;
    this.dragging = false;
  }

  createClass(TouchStrategy, [{
    key: "isDragging",
    value: function isDragging() {
      return this.dragging;
    }
  }, {
    key: "isWaiting",
    value: function isWaiting() {
      return !!this.touchStartTimeout;
    }
  }, {
    key: "cancelWaiting",
    value: function cancelWaiting() {
      clearTimeout(this.touchStartTimeout);
      this.touchStartTimeout = null;
    }
  }, {
    key: "start",
    value: function start(e) {
      var _this = this;

      var _e$touches$ = e.touches[0],
          x = _e$touches$.clientX,
          y = _e$touches$.clientY;

      this.touchStartTimeout = setTimeout(function () {
        _this.delegate.onStart({ x: x, y: y });
        _this.dragging = true;
      }, TIMEOUT);
    }
  }, {
    key: "move",
    value: function move(e) {
      this.cancelWaiting();
      if (this.dragging) {
        var _e$touches$2 = e.touches[0],
            clientX = _e$touches$2.clientX,
            clientY = _e$touches$2.clientY;

        e.preventDefault();
        this.delegate.onMove({ x: clientX, y: clientY });
      }
    }
  }, {
    key: "end",
    value: function end(e) {
      this.cancelWaiting();
      if (this.dragging) {
        var _e$changedTouches$ = e.changedTouches[0],
            clientX = _e$changedTouches$.clientX,
            clientY = _e$changedTouches$.clientY;

        this.delegate.onEnd({ x: clientX, y: clientY });
      }
      this.mouseInitialOffset = null;
      this.dragging = false;
    }
  }]);
  return TouchStrategy;
}();

/* globals document:true */

var gestureCover = null;
var toggleGestureCover = function toggleGestureCover(toggle, cursor) {
  var style = {
    pointerEvents: toggle ? 'all' : 'none'
  };
  if (toggle && cursor) {
    style = _extends({}, style, {
      cursor: cursor
    });
  }
  if (!gestureCover) {
    style = _extends({}, style, {
      position: 'fixed',
      top: 0,
      right: 0,
      left: 0,
      bottom: 0,
      opacity: 0,
      zIndex: 2147483647
    });

    gestureCover = document.createElement('div');
    document.body.appendChild(gestureCover);
  }
  Object.keys(style).forEach(function (key) {
    gestureCover.style[key] = style[key];
  });
};

/* globals window:true document:true */

var BOUNDARY = 10;
var clamp = function clamp(value, min, max) {
  return Math.max(Math.min(value, max), min);
};
var isBoundExceeded = function isBoundExceeded(_ref, _ref2) {
  var initialX = _ref.x,
      initialY = _ref.y;
  var x = _ref2.x,
      y = _ref2.y;
  return clamp(x, initialX - BOUNDARY, initialX + BOUNDARY) !== x || clamp(y, initialY - BOUNDARY, initialY + BOUNDARY) !== y;
};

var MouseStrategy = function () {
  function MouseStrategy(delegate) {
    classCallCheck(this, MouseStrategy);

    this.delegate = delegate;
    this.mouseInitialOffset = null;
    this.dragging = false;
  }

  createClass(MouseStrategy, [{
    key: 'isDragging',
    value: function isDragging() {
      return this.dragging;
    }
  }, {
    key: 'start',
    value: function start(e) {
      var x = e.clientX,
          y = e.clientY;

      this.e = e;
      this.mouseInitialOffset = { x: x, y: y };
    }
  }, {
    key: 'move',
    value: function move(e) {
      var x = e.clientX,
          y = e.clientY;

      var dragStarted = false;
      if (!this.dragging && this.mouseInitialOffset) {
        if (isBoundExceeded(this.mouseInitialOffset, { x: x, y: y })) {
          this.delegate.onStart(this.mouseInitialOffset);
          if (window.getSelection) {
            window.getSelection().removeAllRanges();
          }
          dragStarted = true;
          this.dragging = true;
        }
      }
      if (this.dragging) {
        e.preventDefault();
        this.delegate.onMove({ x: x, y: y });
      }
      if (dragStarted) {
        var _window$getComputedSt = window.getComputedStyle(document.elementFromPoint(x, y)),
            cursor = _window$getComputedSt.cursor;

        toggleGestureCover(true, cursor);
      }
    }
  }, {
    key: 'end',
    value: function end(e) {
      if (this.dragging) {
        var x = e.clientX,
            y = e.clientY;

        toggleGestureCover(false);
        this.delegate.onEnd({ x: x, y: y });
      }
      this.mouseInitialOffset = null;
      this.dragging = false;
    }
  }]);
  return MouseStrategy;
}();

/* globals window:true */

var eventEmitter = null;
var getSharedEventEmitter = function getSharedEventEmitter() {
  if (!eventEmitter) {
    eventEmitter = new EventEmitter();

    ['mousemove', 'mouseup', 'touchmove', 'touchend', 'touchcancel'].forEach(function (name) {
      return window.addEventListener(name, function (e) {
        return eventEmitter.emit([name, e]);
      }, { passive: false });
    });
  }
  return eventEmitter;
};

/* globals document:true window:true */

var clear = function clear() {
  if (window.getSelection) {
    if (window.getSelection().empty) {
      window.getSelection().empty();
    } else if (window.getSelection().removeAllRanges) {
      window.getSelection().removeAllRanges();
    }
  } else if (document.selection) {
    document.selection.empty();
  }
};

// eslint-disable-next-line camelcase
var draggingHandled = Symbol('draggingHandled');

var Draggable = function (_React$Component) {
  inherits(Draggable, _React$Component);

  function Draggable(props, context) {
    classCallCheck(this, Draggable);

    var _this = possibleConstructorReturn(this, (Draggable.__proto__ || Object.getPrototypeOf(Draggable)).call(this, props, context));

    var delegate = {
      onStart: function onStart(_ref) {
        var x = _ref.x,
            y = _ref.y;
        var onStart = _this.props.onStart;

        if (!onStart) return;
        unstable_batchedUpdates(function () {
          onStart({ x: x, y: y });
        });
      },
      onMove: function onMove(_ref2) {
        var x = _ref2.x,
            y = _ref2.y;
        var onUpdate = _this.props.onUpdate;

        if (!onUpdate) return;
        unstable_batchedUpdates(function () {
          onUpdate({ x: x, y: y });
        });
      },
      onEnd: function onEnd(_ref3) {
        var x = _ref3.x,
            y = _ref3.y;
        var onEnd = _this.props.onEnd;

        if (!onEnd) return;
        unstable_batchedUpdates(function () {
          onEnd({ x: x, y: y });
        });
      }
    };

    _this.mouseStrategy = new MouseStrategy(delegate);
    _this.touchStrategy = new TouchStrategy(delegate);

    _this.mouseDownListener = _this.mouseDownListener.bind(_this);
    _this.touchStartListener = _this.touchStartListener.bind(_this);
    _this.globalListener = _this.globalListener.bind(_this);
    return _this;
  }

  createClass(Draggable, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      getSharedEventEmitter().subscribe(this.globalListener);
      this.setupNodeSubscription();
    }
  }, {
    key: 'shouldComponentUpdate',
    value: function shouldComponentUpdate(nextProps) {
      return nextProps.children !== this.props.children;
    }
  }, {
    key: 'componentDidUpdate',
    value: function componentDidUpdate() {
      this.setupNodeSubscription();
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      getSharedEventEmitter().unsubscribe(this.globalListener);
    }
  }, {
    key: 'setupNodeSubscription',
    value: function setupNodeSubscription() {
      // eslint-disable-next-line react/no-find-dom-node
      var node$$1 = findDOMNode(this);
      if (!node$$1) return;
      node$$1.removeEventListener('mousedown', this.mouseDownListener);
      node$$1.removeEventListener('touchstart', this.touchStartListener);
      node$$1.addEventListener('mousedown', this.mouseDownListener, { passive: true });
      node$$1.addEventListener('touchstart', this.touchStartListener, { passive: true });
    }
  }, {
    key: 'mouseDownListener',
    value: function mouseDownListener(e) {
      if (this.touchStrategy.isWaiting() || e[draggingHandled]) return;
      this.mouseStrategy.start(e);
      e[draggingHandled] = true;
    }
  }, {
    key: 'touchStartListener',
    value: function touchStartListener(e) {
      if (e[draggingHandled]) return;
      this.touchStrategy.start(e);
      e[draggingHandled] = true;
    }
  }, {
    key: 'globalListener',
    value: function globalListener(_ref4) {
      var _ref5 = slicedToArray(_ref4, 2),
          name = _ref5[0],
          e = _ref5[1];

      switch (name) {
        case 'mousemove':
          this.mouseStrategy.move(e);
          break;
        case 'mouseup':
          this.mouseStrategy.end(e);
          break;
        case 'touchmove':
          {
            this.touchStrategy.move(e);
            break;
          }
        case 'touchend':
        case 'touchcancel':
          {
            this.touchStrategy.end(e);
            break;
          }
        default:
          break;
      }
      if (this.mouseStrategy.isDragging() || this.touchStrategy.isDragging()) {
        clear();
      }
    }
  }, {
    key: 'render',
    value: function render() {
      return this.props.children;
    }
  }]);
  return Draggable;
}(React.Component);

Draggable.propTypes = {
  children: node.isRequired,
  onStart: func,
  onUpdate: func,
  onEnd: func
};

Draggable.defaultProps = {
  onStart: undefined,
  onUpdate: undefined,
  onEnd: undefined
};

var DragDropProviderCore = function () {
  function DragDropProviderCore() {
    classCallCheck(this, DragDropProviderCore);

    this.payload = null;
    this.dragEmitter = new EventEmitter();
  }

  createClass(DragDropProviderCore, [{
    key: 'start',
    value: function start(payload, clientOffset) {
      this.payload = payload;
      this.dragEmitter.emit({ payload: this.payload, clientOffset: clientOffset });
    }
  }, {
    key: 'update',
    value: function update(clientOffset) {
      this.dragEmitter.emit({ payload: this.payload, clientOffset: clientOffset });
    }
  }, {
    key: 'end',
    value: function end(clientOffset) {
      this.dragEmitter.emit({ payload: this.payload, clientOffset: clientOffset, end: true });
      this.payload = null;
    }
  }]);
  return DragDropProviderCore;
}();

var DragDropProvider = function (_React$Component) {
  inherits(DragDropProvider, _React$Component);

  function DragDropProvider(props) {
    classCallCheck(this, DragDropProvider);

    var _this = possibleConstructorReturn(this, (DragDropProvider.__proto__ || Object.getPrototypeOf(DragDropProvider)).call(this, props));

    _this.dragDropProvider = new DragDropProviderCore();

    _this.dragDropProvider.dragEmitter.subscribe(function (_ref) {
      var payload = _ref.payload,
          clientOffset = _ref.clientOffset,
          end = _ref.end;

      _this.props.onChange({
        payload: end ? null : payload,
        clientOffset: end ? null : clientOffset
      });
    });
    return _this;
  }

  createClass(DragDropProvider, [{
    key: 'getChildContext',
    value: function getChildContext() {
      return {
        dragDropProvider: this.dragDropProvider
      };
    }
  }, {
    key: 'shouldComponentUpdate',
    value: function shouldComponentUpdate(nextProps) {
      return nextProps.children !== this.props.children;
    }
  }, {
    key: 'render',
    value: function render() {
      return this.props.children;
    }
  }]);
  return DragDropProvider;
}(React.Component);

DragDropProvider.childContextTypes = {
  dragDropProvider: object.isRequired
};

DragDropProvider.propTypes = {
  children: node.isRequired,
  onChange: func
};

DragDropProvider.defaultProps = {
  onChange: function onChange() {}
};

var DragSource = function (_React$Component) {
  inherits(DragSource, _React$Component);

  function DragSource() {
    classCallCheck(this, DragSource);
    return possibleConstructorReturn(this, (DragSource.__proto__ || Object.getPrototypeOf(DragSource)).apply(this, arguments));
  }

  createClass(DragSource, [{
    key: 'shouldComponentUpdate',
    value: function shouldComponentUpdate(nextProps) {
      return nextProps.children !== this.props.children;
    }
  }, {
    key: 'render',
    value: function render() {
      var _this2 = this;

      var dragDropProvider = this.context.dragDropProvider;
      var _props = this.props,
          _onStart = _props.onStart,
          _onUpdate = _props.onUpdate,
          _onEnd = _props.onEnd;

      return _(
        Draggable,
        {
          onStart: function onStart(_ref) {
            var x = _ref.x,
                y = _ref.y;

            dragDropProvider.start(_this2.props.payload, { x: x, y: y });
            _onStart({ clientOffset: { x: x, y: y } });
          },
          onUpdate: function onUpdate(_ref2) {
            var x = _ref2.x,
                y = _ref2.y;

            dragDropProvider.update({ x: x, y: y });
            _onUpdate({ clientOffset: { x: x, y: y } });
          },
          onEnd: function onEnd(_ref3) {
            var x = _ref3.x,
                y = _ref3.y;

            dragDropProvider.end({ x: x, y: y });
            _onEnd({ clientOffset: { x: x, y: y } });
          }
        },
        this.props.children
      );
    }
  }]);
  return DragSource;
}(React.Component);

DragSource.contextTypes = {
  dragDropProvider: object.isRequired
};

DragSource.propTypes = {
  children: node.isRequired,
  payload: any.isRequired,
  onStart: func,
  onUpdate: func,
  onEnd: func
};

DragSource.defaultProps = {
  onStart: function onStart() {},
  onUpdate: function onUpdate() {},
  onEnd: function onEnd() {}
};

var clamp$1 = function clamp(value, min, max) {
  return Math.max(Math.min(value, max), min);
};

var DropTarget = function (_React$Component) {
  inherits(DropTarget, _React$Component);

  function DropTarget(props, context) {
    classCallCheck(this, DropTarget);

    var _this = possibleConstructorReturn(this, (DropTarget.__proto__ || Object.getPrototypeOf(DropTarget)).call(this, props, context));

    _this.node = null;
    _this.isOver = false;

    _this.handleDrag = _this.handleDrag.bind(_this);
    return _this;
  }

  createClass(DropTarget, [{
    key: 'componentWillMount',
    value: function componentWillMount() {
      var dragEmitter = this.context.dragDropProvider.dragEmitter;

      dragEmitter.subscribe(this.handleDrag);
    }
  }, {
    key: 'shouldComponentUpdate',
    value: function shouldComponentUpdate(nextProps) {
      return nextProps.children !== this.props.children;
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      var dragEmitter = this.context.dragDropProvider.dragEmitter;

      dragEmitter.unsubscribe(this.handleDrag);
    }
  }, {
    key: 'handleDrag',
    value: function handleDrag(_ref) {
      var payload = _ref.payload,
          clientOffset = _ref.clientOffset,
          end = _ref.end;

      var _findDOMNode$getBound = findDOMNode(this).getBoundingClientRect(),
          left = _findDOMNode$getBound.left,
          top = _findDOMNode$getBound.top,
          right = _findDOMNode$getBound.right,
          bottom = _findDOMNode$getBound.bottom; // eslint-disable-line react/no-find-dom-node


      var isOver = clientOffset && clamp$1(clientOffset.x, left, right) === clientOffset.x && clamp$1(clientOffset.y, top, bottom) === clientOffset.y;

      if (!this.isOver && isOver) this.props.onEnter({ payload: payload, clientOffset: clientOffset });
      if (this.isOver && isOver) this.props.onOver({ payload: payload, clientOffset: clientOffset });
      if (this.isOver && !isOver) this.props.onLeave({ payload: payload, clientOffset: clientOffset });
      if (isOver && end) this.props.onDrop({ payload: payload, clientOffset: clientOffset });

      this.isOver = isOver && !end;
    }
  }, {
    key: 'render',
    value: function render() {
      var children = this.props.children;

      return React.Children.only(children);
    }
  }]);
  return DropTarget;
}(React.Component);

DropTarget.contextTypes = {
  dragDropProvider: object.isRequired
};

DropTarget.propTypes = {
  children: node.isRequired,
  onEnter: func,
  onOver: func,
  onLeave: func,
  onDrop: func
};

DropTarget.defaultProps = {
  onEnter: function onEnter() {},
  onOver: function onOver() {},
  onLeave: function onLeave() {},
  onDrop: function onDrop() {}
};

var createRenderComponent = function createRenderComponent(Component$$1, initialAdditionalProps) {
  var storedAdditionalProps = initialAdditionalProps;
  var components = new Set();

  var RenderComponent = function (_React$Component) {
    inherits(RenderComponent, _React$Component);

    function RenderComponent() {
      classCallCheck(this, RenderComponent);
      return possibleConstructorReturn(this, (RenderComponent.__proto__ || Object.getPrototypeOf(RenderComponent)).apply(this, arguments));
    }

    createClass(RenderComponent, [{
      key: 'componentWillMount',
      value: function componentWillMount() {
        components.add(this);
      }
    }, {
      key: 'componentWillUnmount',
      value: function componentWillUnmount() {
        components.delete(this);
      }
    }, {
      key: 'render',
      value: function render() {
        return _(Component$$1, _extends({}, this.props, storedAdditionalProps));
      }
    }]);
    return RenderComponent;
  }(React.Component);

  return {
    component: RenderComponent,
    update: function update(additionalProps) {
      storedAdditionalProps = additionalProps;
      Array.from(components.values()).forEach(function (component) {
        return component.forceUpdate();
      });
    }
  };
};

return { PluginHost : PluginHost$1, Plugin:Plugin, Action:Action, Getter:Getter, Template:Template
	, TemplatePlaceholder:TemplatePlaceholder, TemplateConnector:TemplateConnector, Draggable:Draggable
	, DragDropProvider:DragDropProvider, DragSource:DragSource, DropTarget:DropTarget, createRenderComponent :createRenderComponent };
}();
//# sourceMappingURL=dx-react-core.es.js.map