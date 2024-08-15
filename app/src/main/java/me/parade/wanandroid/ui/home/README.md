1. 首页架构
    使用BottomNavigationView+FragmentContainerView,使用hide+show的方式切换，第一次显示的时候会调用onResume,再切回来的时候不会调用onResume,会调用onHiddenChanged，但是第一次显示的时候不会调用onHiddenChanged
2. 子页面架构
    * 使用ViewPager2+TabLayout,再VP之间切换时，会调用当前显示的Fragment的onResume。
    * 从后台切回到前台时，ViewPager2的fragment(在缓存区的)都会调用onResume
    * 从父activity的其他fragment切回来的时候，ViewPager2的fragment(在缓存区的)都会调用onHiddenChanged(false)
    * onHiddenChanged(false)和onResume永远不会同时调用
    * 懒加载同时用可见状态和onResume判断
    