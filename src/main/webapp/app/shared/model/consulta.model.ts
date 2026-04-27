import { type IHistoriaClinica } from '@/shared/model/historia-clinica.model';
import { type IMedico } from '@/shared/model/medico.model';
import { type IPaciente } from '@/shared/model/paciente.model';
import { type ITurno } from '@/shared/model/turno.model';

export interface IConsulta {
  id?: number;
  codigo?: string;
  fechaHoraInicio?: Date;
  fechaHoraFin?: Date | null;
  sintomas?: string | null;
  motivoConsulta?: string;
  examenFisico?: string | null;
  observaciones?: string | null;
  indicaciones?: string | null;
  activa?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  turno?: ITurno | null;
  paciente?: IPaciente;
  medico?: IMedico;
  historiaClinica?: IHistoriaClinica;
}

export class Consulta implements IConsulta {
  constructor(
    public id?: number,
    public codigo?: string,
    public fechaHoraInicio?: Date,
    public fechaHoraFin?: Date | null,
    public sintomas?: string | null,
    public motivoConsulta?: string,
    public examenFisico?: string | null,
    public observaciones?: string | null,
    public indicaciones?: string | null,
    public activa?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public turno?: ITurno | null,
    public paciente?: IPaciente,
    public medico?: IMedico,
    public historiaClinica?: IHistoriaClinica,
  ) {
    this.activa = this.activa ?? false;
  }
}
