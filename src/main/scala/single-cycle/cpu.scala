// This file is where all of the CPU components are assembled into the whole CPU

package dinocpu

import chisel3._
import chisel3.util._
import dinocpu.components._

/**
 * The main CPU definition that hooks up all of the other components.
 *
 * For more information, see section 4.4 of Patterson and Hennessy
 * This follows figure 4.21
 */
class SingleCycleCPU(implicit val conf: CPUConfig) extends BaseCPU {
  // All of the structures required
  val pc              = dontTouch(RegInit(0.U(64.W)))
  val control         = Module(new Control())
  val registers       = Module(new RegisterFile())
  val aluControl      = Module(new ALUControl())
  val alu             = Module(new ALU())
  val immGen          = Module(new ImmediateGenerator())
  val controlTransfer = Module(new ControlTransferUnit())
  val (cycleCount, _) = Counter(true.B, 1 << 30)

  control.io := DontCare
  registers.io := DontCare
  aluControl.io := DontCare
  alu.io := DontCare
  immGen.io := DontCare
  controlTransfer.io := DontCare
  io.dmem <> DontCare

  //FETCH
  io.imem.address := pc
  io.imem.valid := true.B

  val instruction = Wire(UInt(32.W))
  when ((pc % 8.U) === 4.U) {
    instruction := io.imem.instruction(63, 32)
  } .otherwise {
    instruction := io.imem.instruction(31, 0)
  }

  //Your code goes here

  // Assign registers to correct bits in instruction 

  control.io.opcode := instruction(6,0)         // Opcode
  registers.io.writereg := instruction(11,7)    // rd
  registers.io.readreg1 := instruction(19,15)   // rs1
  registers.io.readreg2 := instruction(24,20)   // rs2
  aluControl.io.funct3 := instruction(14,12)    // funct 3
  aluControl.io.funct7 := instruction(31,25)    // funct 7

  // Combine all results to send back to registers  
  registers.io.writedata := alu.io.result
  
  // check validity of registers
  registers.io.wen := (registers.io.writereg =/= 0.U) && (control.io.writeback_valid === 1.U)
  
  // Set ALU control signals
  aluControl.io.aluop := control.io.aluop
  alu.io.operation := aluControl.io.operation

  // Connect ALU input and control singals
  alu.io.operand1 := registers.io.readdata1
  alu.io.operand2 := registers.io.readdata2

  pc := pc + 4.U  //Increment PC

}


/*
 * Object to make it easier to print information about the CPU
 */
object SingleCycleCPUInfo {
  def getModules(): List[String] = {
    List(
      "dmem",
      "imem",
      "control",
      "registers",
      "csr",
      "aluControl",
      "alu",
      "immGen",
      "controlTransfer"
    )
  }
}