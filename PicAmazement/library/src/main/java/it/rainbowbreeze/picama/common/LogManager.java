package it.rainbowbreeze.picama.common;

import android.util.Log;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.libs.common.RainbowLogFacility;

/**
 * This file is part of KeepMoving. KeepMoving is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, version 2.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p/>
 * Copyright Alfredo Morresi
 * <p/>
 * Created by Alfredo "Rainbowbreeze" Morresi on 15/06/14.
 */

/**
 * Manages logging
 */
public class LogManager extends RainbowLogFacility {
    public LogManager() {
        this(Bag.APP_NAME_LOG);
    }

    private LogManager(String tag) {
        super(tag);
    }
}
