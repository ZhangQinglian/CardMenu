# CardMenu

CardMenu is a simple and beautiful menu container

# How to include

Add the library to your module **build.gradle**:

dependencies {
    compile compile 'com.zql.android:cardmenulib:1.0'
}

# usage

**layout**
```xml
    <com.zql.android.cardmenulib.CardMenu
        android:id="@+id/cardMenu"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <FrameLayout
            android:id="@+id/main_content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/white">

            <TextView
                android:id="@+id/content_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:textSize="30sp"
                android:textColor="@android:color/black"/>
        </FrameLayout>
    </com.zql.android.cardmenulib.CardMenu>
```
**java**

```java
    int[] names = new int[]{R.string.m_bike,R.string.m_boat,R.string.m_car,R.string.m_airport};
    int[] icons = new int[]{R.mipmap.ic_directions_bike_white_36dp,R.mipmap.ic_directions_boat_white_36dp,R.mipmap.ic_directions_car_white_36dp,R.mipmap.ic_local_airport_white_36dp};
    CardMenu menu = (CardMenu) findViewById(R.id.cardMenu);
    menu.initMenus(names, icons, null);
```

![](http://7xprgn.com1.z0.glb.clouddn.com/2016-04-18%2019_07_39.gif)

#License

     Copyright 2016 zhangqinglian

  	Licensed under the Apache License, Version 2.0 (the "License");
  	you may not use this file except in compliance with the License.
  	You may obtain a copy of the License at

	     http://www.apache.org/licenses/LICENSE-2.0

  	Unless required by applicable law or agreed to in writing, software
	  distributed under the License is distributed on an "AS IS" BASIS,
	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	  See the License for the specific language governing permissions and
	  limitations under the License.

  	CardMenu includes code from cardmenulib, which is 
	  licensed under the MIT license. You may obtain a copy at
	
	     https://github.com/ZhangQinglian/CardMenu/blob/master/LICENSE
