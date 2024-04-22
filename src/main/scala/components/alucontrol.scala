// This file contains ALU control logic.

package dinocpu.components

import chisel3._
import chisel3.util._

/**
 * The ALU control unit
 *
 * Input:  aluop        Specifying the type of instruction using ALU
 *                          . 0 for none of the below
 *                          . 1 for 64-bit R-type
 *                          . 2 for 64-bit I-type
 *                          . 3 for 32-bit R-type
 *                          . 4 for 32-bit I-type
 *                          . 5 for non-arithmetic instruction types that uses ALU (auipc/jal/jarl/Load/Store)
 * Input:  funct7       The most significant bits of the instruction.
 * Input:  funct3       The middle three bits of the instruction (12-14).
 *
 * Output: operation    What we want the ALU to do.
 *
 * For more information, see Section 4.4 and A.5 of Patterson and Hennessy.
 * This is loosely based on figure 4.12
 */
class ALUControl extends Module {
  val io = IO(new Bundle {
    val aluop     = Input(UInt(3.W))
    val funct7    = Input(UInt(7.W))
    val funct3    = Input(UInt(3.W))

    val operation = Output(UInt(5.W))
  })

  io.operation := "b11111".U // Invalid

  //Your code goes here 

//Added Marina
  when(io.aluop === "b001".U) { // 64 bit R-type {  //1 for 64-bit R-type
      is("b0000000000".U) { io.operation := "b00001".U } // ADD
      is("b0100000000".U) { io.operation := "b00010".U } // SUB
      is("b0000000001".U) { io.operation := "b01010".U } // SLL
      is("b0000000010".U) { io.operation := "b01011".U } // SLT
      is("b0000000011".U) { io.operation := "b01100".U } // SLTU
      is("b0000000100".U) { io.operation := "b00011".U } // XOR
      is("b0000000101".U) { io.operation := "b01001".U } // SRL
      is("b0100000101".U) { io.operation := "b01000".U } // SRA
      is("b0000000110".U) { io.operation := "b00100".U } // OR
      is("b0000000111".U) { io.operation := "b00101".U } // AND
      is("b0000001000".U) { io.operation := "b01101".U } // MUL
      is("b0000001001".U) { io.operation := "b01110".U } // MULH
      is("b0000001010".U) { io.operation := "b01111".U } // MULHSU
      is("b0000001011".U) { io.operation := "b10000".U } // MULHU
      is("b0000001100".U) { io.operation := "b10001".U } // DIV
      is("b0000001101".U) { io.operation := "b10010".U } // DIVU
      is("b0000001110".U) { io.operation := "b10011".U } // REM
      is("b0000001111".U) { io.operation := "b10100".U } // REMU
  } 

when(io.aluop === "b011".U) { // 32 bit R-type  //3 for 32-bit R-type
  is("b0000000000".U) { io.operation := "b00000".U } // ADDW
  is("b0100000000".U) { io.operation := "b00010".U } // SUBW
  is("b0000000001".U) { io.operation := "b10011".U } // SLLW
  is("b0000000101".U) { io.operation := "b10101".U } // SRLW
  is("b0100000101".U) { io.operation := "b10001".U } // SRAW
  is("b0000001000".U) { io.operation := "b00101".U } //MULW
  is("b0000001100".U) { io.operation := "b01001".U } // DIVW
  is("b0000001101".U) { io.operation := "b01100".U }  //DIVUW
  is("b0000001110".U) { io.operation := "b11010".U }  // REMW
  is("b0000001111".U) { io.operation := "b11001".U }  // REMUW
    }
  
  }