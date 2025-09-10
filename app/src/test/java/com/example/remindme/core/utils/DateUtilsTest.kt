package com.example.remindme.core.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import java.util.Calendar
import kotlin.test.Test

class DateUtilsTest {
    @Test
    fun testFormatDueDate_null() {
        assertEquals("(Press to Edit) No Due Date", formatDueDateToEditString(null))
    }

    @Test
    fun testFormatDueDate_validMillis() {
        val millis = 123456789L
        assertEquals("(Press to Edit) Due Date: 1970-01-02", formatDueDateToEditString(millis))
    }

//    @Test
//    fun testFormatDueDateReadable_withDate() {
//        val millis = 0L // 1970-01-01
//        val result = formatDueDateReadable(millis)
//        assertNotNull(result)
//        assertTrue(result!!.contains("1970")) // or exact format match
//    }
//
//    @Test
//    fun testFormatDueDateReadable_nullDate() {
//        val result = formatDueDateReadable(null)
//        assertNull(result)
//    }

    @Test
    fun `returns formatted date when selectedDateMillis is not null`() {
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.SEPTEMBER, 10) // September 10, 2025
        val millis = calendar.timeInMillis

        val result = getFormattedDueDateText(millis)

        // Expected format: "Due Date: Wed, 10 Sep 2025"
        assert(result.startsWith("Due Date:"))
        assert(result.contains("10 Sep 2025"))
    }

    @Test
    fun `returns no due date message when selectedDateMillis is null`() {
        val result = getFormattedDueDateText(null)
        assertEquals("No Due Date Selected", result)
    }

    @Test
    fun testGetDueDateWithNullCheck_null() {
        assertEquals(-1L, getDueDateWithNullCheck(null))
    }

    @Test
    fun testGetDueDateWithNullCheck_validDueDate() {
        assertEquals(12345678L, getDueDateWithNullCheck(12345678L))
    }

    @Test
    fun testGetTimeDueWithDateDue_invalid() {
        assertEquals(-1L, getTimeDueWithDateDue(-1L))
    }

    @Test
    fun testGetTimeDueWithDateDue_valid() {
        assertEquals(123456789L-3600000, getTimeDueWithDateDue(123456789L))
    }
}