!function(t, e) {
    "object" == typeof exports && "undefined" != typeof module ? e(require("jquery")) : "function" == typeof define && define.amd ? define(["jquery"], e) : e((t = t || self).jQuery)
}(this, (function(t) {
    "use strict";
    t = t && t.hasOwnProperty("default") ? t.default : t;
    var e = "undefined" != typeof globalThis ? globalThis : "undefined" != typeof window ? window : "undefined" != typeof global ? global : "undefined" != typeof self ? self : {};
    function n(t, e) {
        return t(e = {
            exports: {}
        }, e.exports),
        e.exports
    }
    var r = function(t) {
        return t && t.Math == Math && t
    }
      , o = r("object" == typeof globalThis && globalThis) || r("object" == typeof window && window) || r("object" == typeof self && self) || r("object" == typeof e && e) || Function("return this")()
      , i = function(t) {
        try {
            return !!t()
        } catch (t) {
            return !0
        }
    }
      , c = !i((function() {
        return 7 != Object.defineProperty({}, "a", {
            get: function() {
                return 7
            }
        }).a
    }
    ))
      , a = {}.propertyIsEnumerable
      , f = Object.getOwnPropertyDescriptor
      , u = {
        f: f && !a.call({
            1: 2
        }, 1) ? function(t) {
            var e = f(this, t);
            return !!e && e.enumerable
        }
        : a
    }
      , s = function(t, e) {
        return {
            enumerable: !(1 & t),
            configurable: !(2 & t),
            writable: !(4 & t),
            value: e
        }
    }
      , l = {}.toString
      , p = function(t) {
        return l.call(t).slice(8, -1)
    }
      , d = "".split
      , y = i((function() {
        return !Object("z").propertyIsEnumerable(0)
    }
    )) ? function(t) {
        return "String" == p(t) ? d.call(t, "") : Object(t)
    }
    : Object
      , h = function(t) {
        if (null == t)
            throw TypeError("Can't call method on " + t);
        return t
    }
      , v = function(t) {
        return y(h(t))
    }
      , b = function(t) {
        return "object" == typeof t ? null !== t : "function" == typeof t
    }
      , g = function(t, e) {
        if (!b(t))
            return t;
        var n, r;
        if (e && "function" == typeof (n = t.toString) && !b(r = n.call(t)))
            return r;
        if ("function" == typeof (n = t.valueOf) && !b(r = n.call(t)))
            return r;
        if (!e && "function" == typeof (n = t.toString) && !b(r = n.call(t)))
            return r;
        throw TypeError("Can't convert object to primitive value")
    }
      , m = {}.hasOwnProperty
      , k = function(t, e) {
        return m.call(t, e)
    }
      , w = o.document
      , O = b(w) && b(w.createElement)
      , j = function(t) {
        return O ? w.createElement(t) : {}
    }
      , S = !c && !i((function() {
        return 7 != Object.defineProperty(j("div"), "a", {
            get: function() {
                return 7
            }
        }).a
    }
    ))
      , $ = Object.getOwnPropertyDescriptor
      , T = {
        f: c ? $ : function(t, e) {
            if (t = v(t),
            e = g(e, !0),
            S)
                try {
                    return $(t, e)
                } catch (t) {}
            if (k(t, e))
                return s(!u.f.call(t, e), t[e])
        }
    }
      , _ = function(t) {
        if (!b(t))
            throw TypeError(String(t) + " is not an object");
        return t
    }
      , P = Object.defineProperty
      , C = {
        f: c ? P : function(t, e, n) {
            if (_(t),
            e = g(e, !0),
            _(n),
            S)
                try {
                    return P(t, e, n)
                } catch (t) {}
            if ("get"in n || "set"in n)
                throw TypeError("Accessors not supported");
            return "value"in n && (t[e] = n.value),
            t
        }
    }
      , E = c ? function(t, e, n) {
        return C.f(t, e, s(1, n))
    }
    : function(t, e, n) {
        return t[e] = n,
        t
    }
      , H = function(t, e) {
        try {
            E(o, t, e)
        } catch (n) {
            o[t] = e
        }
        return e
    }
      , x = o["__core-js_shared__"] || H("__core-js_shared__", {})
      , A = Function.toString;
    "function" != typeof x.inspectSource && (x.inspectSource = function(t) {
        return A.call(t)
    }
    );
    var M, B, L, I = x.inspectSource, R = o.WeakMap, q = "function" == typeof R && /native code/.test(I(R)), F = n((function(t) {
        (t.exports = function(t, e) {
            return x[t] || (x[t] = void 0 !== e ? e : {})
        }
        )("versions", []).push({
            version: "3.6.0",
            mode: "global",
            copyright: "© 2019 Denis Pushkarev (zloirock.ru)"
        })
    }
    )), z = 0, N = Math.random(), Y = function(t) {
        return "Symbol(" + String(void 0 === t ? "" : t) + ")_" + (++z + N).toString(36)
    }, D = F("keys"), W = function(t) {
        return D[t] || (D[t] = Y(t))
    }, X = {}, V = o.WeakMap;
    if (q) {
        var G = new V
          , K = G.get
          , Q = G.has
          , J = G.set;
        M = function(t, e) {
            return J.call(G, t, e),
            e
        }
        ,
        B = function(t) {
            return K.call(G, t) || {}
        }
        ,
        L = function(t) {
            return Q.call(G, t)
        }
    } else {
        var U = W("state");
        X[U] = !0,
        M = function(t, e) {
            return E(t, U, e),
            e
        }
        ,
        B = function(t) {
            return k(t, U) ? t[U] : {}
        }
        ,
        L = function(t) {
            return k(t, U)
        }
    }
    var Z, tt, et = {
        set: M,
        get: B,
        has: L,
        enforce: function(t) {
            return L(t) ? B(t) : M(t, {})
        },
        getterFor: function(t) {
            return function(e) {
                var n;
                if (!b(e) || (n = B(e)).type !== t)
                    throw TypeError("Incompatible receiver, " + t + " required");
                return n
            }
        }
    }, nt = n((function(t) {
        var e = et.get
          , n = et.enforce
          , r = String(String).split("String");
        (t.exports = function(t, e, i, c) {
            var a = !!c && !!c.unsafe
              , f = !!c && !!c.enumerable
              , u = !!c && !!c.noTargetGet;
            "function" == typeof i && ("string" != typeof e || k(i, "name") || E(i, "name", e),
            n(i).source = r.join("string" == typeof e ? e : "")),
            t !== o ? (a ? !u && t[e] && (f = !0) : delete t[e],
            f ? t[e] = i : E(t, e, i)) : f ? t[e] = i : H(e, i)
        }
        )(Function.prototype, "toString", (function() {
            return "function" == typeof this && e(this).source || I(this)
        }
        ))
    }
    )), rt = o, ot = function(t) {
        return "function" == typeof t ? t : void 0
    }, it = function(t, e) {
        return arguments.length < 2 ? ot(rt[t]) || ot(o[t]) : rt[t] && rt[t][e] || o[t] && o[t][e]
    }, ct = Math.ceil, at = Math.floor, ft = function(t) {
        return isNaN(t = +t) ? 0 : (t > 0 ? at : ct)(t)
    }, ut = Math.min, st = function(t) {
        return t > 0 ? ut(ft(t), 9007199254740991) : 0
    }, lt = Math.max, pt = Math.min, dt = function(t) {
        return function(e, n, r) {
            var o, i = v(e), c = st(i.length), a = function(t, e) {
                var n = ft(t);
                return n < 0 ? lt(n + e, 0) : pt(n, e)
            }(r, c);
            if (t && n != n) {
                for (; c > a; )
                    if ((o = i[a++]) != o)
                        return !0
            } else
                for (; c > a; a++)
                    if ((t || a in i) && i[a] === n)
                        return t || a || 0;
            return !t && -1
        }
    }, yt = {
        includes: dt(!0),
        indexOf: dt(!1)
    }.indexOf, ht = function(t, e) {
        var n, r = v(t), o = 0, i = [];
        for (n in r)
            !k(X, n) && k(r, n) && i.push(n);
        for (; e.length > o; )
            k(r, n = e[o++]) && (~yt(i, n) || i.push(n));
        return i
    }, vt = ["constructor", "hasOwnProperty", "isPrototypeOf", "propertyIsEnumerable", "toLocaleString", "toString", "valueOf"], bt = vt.concat("length", "prototype"), gt = {
        f: Object.getOwnPropertyNames || function(t) {
            return ht(t, bt)
        }
    }, mt = {
        f: Object.getOwnPropertySymbols
    }, kt = it("Reflect", "ownKeys") || function(t) {
        var e = gt.f(_(t))
          , n = mt.f;
        return n ? e.concat(n(t)) : e
    }
    , wt = function(t, e) {
        for (var n = kt(e), r = C.f, o = T.f, i = 0; i < n.length; i++) {
            var c = n[i];
            k(t, c) || r(t, c, o(e, c))
        }
    }, Ot = /#|\.prototype\./, jt = function(t, e) {
        var n = $t[St(t)];
        return n == _t || n != Tt && ("function" == typeof e ? i(e) : !!e)
    }, St = jt.normalize = function(t) {
        return String(t).replace(Ot, ".").toLowerCase()
    }
    , $t = jt.data = {}, Tt = jt.NATIVE = "N", _t = jt.POLYFILL = "P", Pt = jt, Ct = T.f, Et = function(t, e) {
        var n, r, i, c, a, f = t.target, u = t.global, s = t.stat;
        if (n = u ? o : s ? o[f] || H(f, {}) : (o[f] || {}).prototype)
            for (r in e) {
                if (c = e[r],
                i = t.noTargetGet ? (a = Ct(n, r)) && a.value : n[r],
                !Pt(u ? r : f + (s ? "." : "#") + r, t.forced) && void 0 !== i) {
                    if (typeof c == typeof i)
                        continue;
                    wt(c, i)
                }
                (t.sham || i && i.sham) && E(c, "sham", !0),
                nt(n, r, c, t)
            }
    }, Ht = Array.isArray || function(t) {
        return "Array" == p(t)
    }
    , xt = function(t) {
        return Object(h(t))
    }, At = function(t, e, n) {
        var r = g(e);
        r in t ? C.f(t, r, s(0, n)) : t[r] = n
    }, Mt = !!Object.getOwnPropertySymbols && !i((function() {
        return !String(Symbol())
    }
    )), Bt = Mt && !Symbol.sham && "symbol" == typeof Symbol(), Lt = F("wks"), It = o.Symbol, Rt = Bt ? It : Y, qt = function(t) {
        return k(Lt, t) || (Mt && k(It, t) ? Lt[t] = It[t] : Lt[t] = Rt("Symbol." + t)),
        Lt[t]
    }, Ft = qt("species"), zt = function(t, e) {
        var n;
        return Ht(t) && ("function" != typeof (n = t.constructor) || n !== Array && !Ht(n.prototype) ? b(n) && null === (n = n[Ft]) && (n = void 0) : n = void 0),
        new (void 0 === n ? Array : n)(0 === e ? 0 : e)
    }, Nt = it("navigator", "userAgent") || "", Yt = o.process, Dt = Yt && Yt.versions, Wt = Dt && Dt.v8;
    Wt ? tt = (Z = Wt.split("."))[0] + Z[1] : Nt && (!(Z = Nt.match(/Edge\/(\d+)/)) || Z[1] >= 74) && (Z = Nt.match(/Chrome\/(\d+)/)) && (tt = Z[1]);
    var Xt, Vt = tt && +tt, Gt = qt("species"), Kt = qt("isConcatSpreadable"), Qt = Vt >= 51 || !i((function() {
        var t = [];
        return t[Kt] = !1,
        t.concat()[0] !== t
    }
    )), Jt = (Xt = "concat",
    Vt >= 51 || !i((function() {
        var t = [];
        return (t.constructor = {})[Gt] = function() {
            return {
                foo: 1
            }
        }
        ,
        1 !== t[Xt](Boolean).foo
    }
    ))), Ut = function(t) {
        if (!b(t))
            return !1;
        var e = t[Kt];
        return void 0 !== e ? !!e : Ht(t)
    };
    Et({
        target: "Array",
        proto: !0,
        forced: !Qt || !Jt
    }, {
        concat: function(t) {
            var e, n, r, o, i, c = xt(this), a = zt(c, 0), f = 0;
            for (e = -1,
            r = arguments.length; e < r; e++)
                if (i = -1 === e ? c : arguments[e],
                Ut(i)) {
                    if (f + (o = st(i.length)) > 9007199254740991)
                        throw TypeError("Maximum allowed index exceeded");
                    for (n = 0; n < o; n++,
                    f++)
                        n in i && At(a, f, i[n])
                } else {
                    if (f >= 9007199254740991)
                        throw TypeError("Maximum allowed index exceeded");
                    At(a, f++, i)
                }
            return a.length = f,
            a
        }
    });
    var Zt, te = function(t, e, n) {
        if (function(t) {
            if ("function" != typeof t)
                throw TypeError(String(t) + " is not a function")
        }(t),
        void 0 === e)
            return t;
        switch (n) {
        case 0:
            return function() {
                return t.call(e)
            }
            ;
        case 1:
            return function(n) {
                return t.call(e, n)
            }
            ;
        case 2:
            return function(n, r) {
                return t.call(e, n, r)
            }
            ;
        case 3:
            return function(n, r, o) {
                return t.call(e, n, r, o)
            }
        }
        return function() {
            return t.apply(e, arguments)
        }
    }, ee = [].push, ne = function(t) {
        var e = 1 == t
          , n = 2 == t
          , r = 3 == t
          , o = 4 == t
          , i = 6 == t
          , c = 5 == t || i;
        return function(a, f, u, s) {
            for (var l, p, d = xt(a), h = y(d), v = te(f, u, 3), b = st(h.length), g = 0, m = s || zt, k = e ? m(a, b) : n ? m(a, 0) : void 0; b > g; g++)
                if ((c || g in h) && (p = v(l = h[g], g, d),
                t))
                    if (e)
                        k[g] = p;
                    else if (p)
                        switch (t) {
                        case 3:
                            return !0;
                        case 5:
                            return l;
                        case 6:
                            return g;
                        case 2:
                            ee.call(k, l)
                        }
                    else if (o)
                        return !1;
            return i ? -1 : r || o ? o : k
        }
    }, re = {
        forEach: ne(0),
        map: ne(1),
        filter: ne(2),
        some: ne(3),
        every: ne(4),
        find: ne(5),
        findIndex: ne(6)
    }, oe = Object.keys || function(t) {
        return ht(t, vt)
    }
    , ie = c ? Object.defineProperties : function(t, e) {
        _(t);
        for (var n, r = oe(e), o = r.length, i = 0; o > i; )
            C.f(t, n = r[i++], e[n]);
        return t
    }
    , ce = it("document", "documentElement"), ae = W("IE_PROTO"), fe = function() {}, ue = function(t) {
        return "<script>" + t + "<\/script>"
    }, se = function() {
        try {
            Zt = document.domain && new ActiveXObject("htmlfile")
        } catch (t) {}
        var t, e;
        se = Zt ? function(t) {
            t.write(ue("")),
            t.close();
            var e = t.parentWindow.Object;
            return t = null,
            e
        }(Zt) : ((e = j("iframe")).style.display = "none",
        ce.appendChild(e),
        e.src = String("javascript:"),
        (t = e.contentWindow.document).open(),
        t.write(ue("document.F=Object")),
        t.close(),
        t.F);
        for (var n = vt.length; n--; )
            delete se.prototype[vt[n]];
        return se()
    };
    X[ae] = !0;
    var le = Object.create || function(t, e) {
        var n;
        return null !== t ? (fe.prototype = _(t),
        n = new fe,
        fe.prototype = null,
        n[ae] = t) : n = se(),
        void 0 === e ? n : ie(n, e)
    }
      , pe = qt("unscopables")
      , de = Array.prototype;
    null == de[pe] && C.f(de, pe, {
        configurable: !0,
        value: le(null)
    });
    var ye, he = re.find, ve = !0;
    function be(t, e) {
        for (var n = 0; n < e.length; n++) {
            var r = e[n];
            r.enumerable = r.enumerable || !1,
            r.configurable = !0,
            "value"in r && (r.writable = !0),
            Object.defineProperty(t, r.key, r)
        }
    }
    function ge(t) {
        return (ge = Object.setPrototypeOf ? Object.getPrototypeOf : function(t) {
            return t.__proto__ || Object.getPrototypeOf(t)
        }
        )(t)
    }
    function me(t, e) {
        return (me = Object.setPrototypeOf || function(t, e) {
            return t.__proto__ = e,
            t
        }
        )(t, e)
    }
    function ke(t, e) {
        return !e || "object" != typeof e && "function" != typeof e ? function(t) {
            if (void 0 === t)
                throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
            return t
        }(t) : e
    }
    function we(t, e, n) {
        return (we = "undefined" != typeof Reflect && Reflect.get ? Reflect.get : function(t, e, n) {
            var r = function(t, e) {
                for (; !Object.prototype.hasOwnProperty.call(t, e) && null !== (t = ge(t)); )
                    ;
                return t
            }(t, e);
            if (r) {
                var o = Object.getOwnPropertyDescriptor(r, e);
                return o.get ? o.get.call(n) : o.value
            }
        }
        )(t, e, n || t)
    }
    "find"in [] && Array(1).find((function() {
        ve = !1
    }
    )),
    Et({
        target: "Array",
        proto: !0,
        forced: ve
    }, {
        find: function(t) {
            return he(this, t, arguments.length > 1 ? arguments[1] : void 0)
        }
    }),
    ye = "find",
    de[pe][ye] = !0;
    t.fn.bootstrapTable.utils;
    t.extend(t.fn.bootstrapTable.defaults, {
        stickyHeader: !1,
        stickyHeaderOffsetY: 0,
        stickyHeaderOffsetLeft: 0,
        stickyHeaderOffsetRight: 0
    }),
    t.BootstrapTable = function(e) {
        function n() {
            return function(t, e) {
                if (!(t instanceof e))
                    throw new TypeError("Cannot call a class as a function")
            }(this, n),
            ke(this, ge(n).apply(this, arguments))
        }
        var r, o, i;
        return function(t, e) {
            if ("function" != typeof e && null !== e)
                throw new TypeError("Super expression must either be null or a function");
            t.prototype = Object.create(e && e.prototype, {
                constructor: {
                    value: t,
                    writable: !0,
                    configurable: !0
                }
            }),
            e && me(t, e)
        }(n, e),
        r = n,
        (o = [{
            key: "initHeader",
            value: function() {
                for (var e, r = this, o = arguments.length, i = new Array(o), c = 0; c < o; c++)
                    i[c] = arguments[c];
                (e = we(ge(n.prototype), "initHeader", this)).call.apply(e, [this].concat(i)),
                this.options.stickyHeader && (this.$el.before('<div class="sticky-header-container"></div>'),
                this.$el.before('<div class="sticky_anchor_begin"></div>'),
                this.$el.after('<div class="sticky_anchor_end"></div>'),
                this.$header.addClass("sticky-header"),
                this.$stickyContainer = this.$tableBody.find(".sticky-header-container"),
                this.$stickyBegin = this.$tableBody.find(".sticky_anchor_begin"),
                this.$stickyEnd = this.$tableBody.find(".sticky_anchor_end"),
                this.$stickyHeader = this.$header.clone(!0, !0),
                t(window).off("resize.sticky-header-table").on("resize.sticky-header-table", (function() {
                    return r.renderStickyHeader()
                }
                )),
                t(window).off("scroll.sticky-header-table").on("scroll.sticky-header-table", (function() {
                    return r.renderStickyHeader()
                }
                )),
                this.$tableBody.off("scroll").on("scroll", (function() {
                    return r.matchPositionX()
                }
                )))
            }
        }, {
            key: "onColumnSearch",
            value: function(t) {
                var e = t.currentTarget
                  , r = t.keyCode;
                we(ge(n.prototype), "onColumnSearch", this).call(this, {
                    currentTarget: e,
                    keyCode: r
                }),
                this.renderStickyHeader()
            }
        }, {
            key: "resetView",
            value: function() {
                for (var e, r = this, o = arguments.length, i = new Array(o), c = 0; c < o; c++)
                    i[c] = arguments[c];
                (e = we(ge(n.prototype), "resetView", this)).call.apply(e, [this].concat(i)),
                t(".bootstrap-table.fullscreen").off("scroll").on("scroll", (function() {
                    return r.renderStickyHeader()
                }
                ))
            }
        }, {
            key: "renderStickyHeader",
            value: function() {
                var e = this
                  , n = this;
                this.$stickyHeader = this.$header.clone(!0, !0),
                this.options.filterControl && t(this.$stickyHeader).off("keyup change mouseup").on("keyup change mouse", (function(e) {
                    var r = t(e.target)
                      , o = r.val()
                      , i = r.parents("th").data("field")
                      , c = n.$header.find('th[data-field="' + i + '"]');
                    if (r.is("input"))
                        c.find("input").val(o);
                    else if (r.is("select")) {
                        var a = c.find("select");
                        a.find("option[selected]").removeAttr("selected"),
                        a.find('option[value="' + o + '"]').attr("selected", !0)
                    }
                    n.triggerSearch()
                }
                ));
                var r = t(window).scrollTop()
                  , o = this.$stickyBegin.offset().top - this.options.stickyHeaderOffsetY
                  , i = this.$stickyEnd.offset().top - this.options.stickyHeaderOffsetY - this.$header.height();
                if (r > o && r <= i) {
                    this.$stickyHeader.find("tr:eq(0)").find("th").each((function(n, r) {
                        t(r).css("min-width", e.$header.find("tr:eq(0)").find("th").eq(n).css("width"))
                    }
                    )),
                    this.$stickyContainer.show().addClass("fix-sticky fixed-table-container");
                    var c = this.options.stickyHeaderOffsetLeft
                      , a = this.options.stickyHeaderOffsetRight;
                    this.$el.closest(".bootstrap-table").hasClass("fullscreen") && (c = 0,
                    a = 0),
                    this.$stickyContainer.css("top", "".concat(this.options.stickyHeaderOffsetY)),
                    this.$stickyContainer.css("left", "".concat(c)),
                    this.$stickyContainer.css("right", "".concat(a)),
                    this.$stickyTable = t("<table/>"),
                    this.$stickyTable.addClass(this.options.classes),
                    this.$stickyTable.addClass("table-sm"),
                    this.$stickyContainer.html(this.$stickyTable.append(this.$stickyHeader)),
                    this.matchPositionX()
                } else
                    this.$stickyContainer.removeClass("fix-sticky").hide()
            }
        }, {
            key: "matchPositionX",
            value: function() {
                this.$stickyContainer.scrollLeft(this.$tableBody.scrollLeft())
            }
        }]) && be(r.prototype, o),
        i && be(r, i),
        n
    }(t.BootstrapTable)
}
));

