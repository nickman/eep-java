// Copyright (c) 2013 Darach Ennis < darach at gmail dot com >.
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions:  
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.

package io.darach.eep;

import java.util.LinkedList;
import java.util.List;

public class TumblingWindow<In,Out> implements EventWindow<In,Out> {	
	private final AggregateFunction<In,Out> fn;
	private final int size;
	private int idx;
	private List<EventEmitter<Out>> outListeners = new LinkedList<EventEmitter<Out>>();

	public TumblingWindow(AggregateFunction<In, Out> fn, int size) {
		this.fn = fn;
		this.size = size;
	}

	public void push(In e) {
		fn.accumulate(e);
		idx++;
		if (idx == size) {
			emit(fn.emit());
			fn.init();
			idx = 0;
		}
	}

	public void emit(Out r) {
		for (EventEmitter<Out> l : outListeners) {
			l.emit(r);
		}
	}

	public void onEmit(EventEmitter<Out> l) {
		outListeners.add(l);
	}

	public void tick() {
		// do nothing
	}
}