package com.purdynet.graph

import com.purdynet.graph.ColumnType.ColumnType

class PFColumn(var lowBoxIdx: Integer, var highBoxIdx: Integer, var columnType: ColumnType) {
  def getLowBoxIdx = lowBoxIdx
  def getHighBoxIdx = highBoxIdx
  def getColumnType = columnType

  def setLowBoxIdx(lowBoxIdx: Integer) = this.lowBoxIdx = lowBoxIdx
  def setHighBoxIdx(highBoxIdx: Integer) = this.highBoxIdx = highBoxIdx
  def setColumnType(columnType: ColumnType) = this.columnType = columnType

  def getCode: String = if (columnType == ColumnType.X) getNumber.toString else "-" + getNumber.toString
  def getNumber: Integer = highBoxIdx - lowBoxIdx + 1

  def copy: PFColumn = new PFColumn(lowBoxIdx, highBoxIdx, columnType)

  override def toString: String = getNumber + columnType.toString
}
