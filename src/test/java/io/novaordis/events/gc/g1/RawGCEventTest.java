/*
 * Copyright (c) 2017 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.events.gc.g1;

import io.novaordis.events.api.gc.GCEvent;
import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class RawGCEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // toGCEvent() -----------------------------------------------------------------------------------------------------

    @Test
    public void toGCEvent() throws Exception {

        String rawContent =
                "[GC pause (G1 Evacuation Pause) (young), 0.7919126 secs]\n" +
                "   [Parallel Time: 254.9 ms, GC Workers: 8]\n" +
                "      [GC Worker Start (ms): Min: 7896.4, Avg: 7908.4, Max: 7932.6, Diff: 36.2]\n" +
                "      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.7, Max: 4.9, Diff: 4.9, Sum: 5.2]\n" +
                "      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]\n" +
                "         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]\n" +
                "      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.3]\n" +
                "      [Code Root Scanning (ms): Min: 0.0, Avg: 0.6, Max: 3.2, Diff: 3.2, Sum: 4.6]\n" +
                "      [Object Copy (ms): Min: 0.0, Avg: 28.4, Max: 63.3, Diff: 63.3, Sum: 227.4]\n" +
                "      [Termination (ms): Min: 0.1, Avg: 60.3, Max: 188.8, Diff: 188.8, Sum: 482.6]\n" +
                "         [Termination Attempts: Min: 1, Avg: 71.0, Max: 178, Diff: 177, Sum: 568]\n" +
                "      [GC Worker Other (ms): Min: 0.1, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.7]\n" +
                "      [GC Worker Total (ms): Min: 59.4, Avg: 90.1, Max: 189.0, Diff: 129.6, Sum: 720.8]\n" +
                "      [GC Worker End (ms): Min: 7968.2, Avg: 7998.5, Max: 8121.6, Diff: 153.3]\n" +
                "   [Code Root Fixup: 0.6 ms]\n" +
                "   [Code Root Purge: 0.1 ms]\n" +
                "   [Clear CT: 167.2 ms]\n" +
                "   [Other: 369.2 ms]\n" +
                "      [Choose CSet: 0.0 ms]\n" +
                "      [Ref Proc: 290.4 ms]\n" +
                "      [Ref Enq: 0.2 ms]\n" +
                "      [Redirty Cards: 77.4 ms]\n" +
                "      [Humongous Register: 0.2 ms]\n" +
                "      [Humongous Reclaim: 0.0 ms]\n" +
                "      [Free CSet: 0.3 ms]\n" +
                "   [Eden: 256.0M(256.0M)->0.0B(230.0M) Survivors: 0.0B->26.0M Heap: 256.0M(5120.0M)->24.2M(5120.0M)]\n" +
                " [Times: user=0.64 sys=0.06, real=0.79 secs] \n";

        Timestamp ts = new TimestampImpl(123L);
        Time t = new Time(ts, 0L);

        RawGCEvent re = new RawGCEvent(t, 779L);

        re.append(rawContent);

        GCEvent e = RawGCEvent.toGCEvent(re);

        Timestamp ts2 = e.getTimestamp();
        assertEquals(ts, ts2);

        assertEquals(779L, e.getLineNumber().longValue());
    }

    // append() --------------------------------------------------------------------------------------------------------

    @Test
    public void append() throws Exception {

        RawGCEvent e = new RawGCEvent(new Time(null, 0L), 1L);

        assertNull(e.getContent());

        e.append("A");

        assertEquals("A", e.getContent());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
