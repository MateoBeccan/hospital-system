import { type ICanalSolicitud } from '@/shared/model/canal-solicitud.model';
import { type IEspecialidad } from '@/shared/model/especialidad.model';
import { type IEstadoTurno } from '@/shared/model/estado-turno.model';
import { type IMedico } from '@/shared/model/medico.model';
import { type IPaciente } from '@/shared/model/paciente.model';

export interface ITurno {
  id?: number;
  codigo?: string;
  fechaHora?: Date;
  duracionMinutos?: number;
  motivoConsulta?: string;
  observaciones?: string | null;
  fechaCreacion?: Date;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  paciente?: IPaciente;
  medico?: IMedico;
  especialidad?: IEspecialidad;
  estadoTurno?: IEstadoTurno;
  canalSolicitud?: ICanalSolicitud;
}

export class Turno implements ITurno {
  constructor(
    public id?: number,
    public codigo?: string,
    public fechaHora?: Date,
    public duracionMinutos?: number,
    public motivoConsulta?: string,
    public observaciones?: string | null,
    public fechaCreacion?: Date,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public paciente?: IPaciente,
    public medico?: IMedico,
    public especialidad?: IEspecialidad,
    public estadoTurno?: IEstadoTurno,
    public canalSolicitud?: ICanalSolicitud,
  ) {
    this.activo = this.activo ?? false;
  }
}
