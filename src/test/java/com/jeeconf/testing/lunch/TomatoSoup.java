package com.jeeconf.testing.lunch;

import com.jeeconf.annotations.JEEConfComponent;
import com.jeeconf.annotations.JEEConfComponentType;
import com.jeeconf.annotations.Key;

@JEEConfComponent
@JEEConfComponentType(Starter.class)
@Key("simple-soupe")
public class TomatoSoup implements Starter {
}
