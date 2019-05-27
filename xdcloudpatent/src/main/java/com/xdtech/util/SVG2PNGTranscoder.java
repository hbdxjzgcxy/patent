package com.xdtech.util;

import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.gvt.renderer.MacRenderer;
import org.apache.batik.transcoder.image.PNGTranscoder;

/**
 * SVG转PNG时，Window服务器上输出空白，需要扩展PNGTranscoder。
 * 
 * @author coolBoy
 *
 */
public class SVG2PNGTranscoder extends PNGTranscoder {
	@Override
	protected ImageRenderer createRenderer() {
		return new MacRenderer();
	}
}
