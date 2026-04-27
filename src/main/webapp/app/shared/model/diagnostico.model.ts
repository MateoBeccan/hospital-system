import { type IConsulta } from '@/shared/model/consulta.model';
import { type IEstadoDiagnostico } from '@/shared/model/estado-diagnostico.model';
import { type IMedico } from '@/shared/model/medico.model';
import { type IPaciente } from '@/shared/model/paciente.model';
import { type ITipoDiagnostico } from '@/shared/model/tipo-diagnostico.model';

export interface IDiagnostico {
  id?: number;
  codigo?: string;
  fechaDiagnostico?: Date;
  descripcion?: string;
  observaciones?: string | null;
  activo?: boolean;
  fechaResolucion?: Date | null;
  esPrincipal?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  consulta?: IConsulta;
  paciente?: IPaciente;
  medico?: IMedico;
  tipoDiagnostico?: ITipoDiagnostico;
  estadoDiagnostico?: IEstadoDiagnostico;
}

export class Diagnostico implements IDiagnostico {
  constructor(
    public id?: number,
    public codigo?: string,
    public fechaDiagnostico?: Date,
    public descripcion?: string,
    public observaciones?: string | null,
    public activo?: boolean,
    public fechaResolucion?: Date | null,
    public esPrincipal?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public consulta?: IConsulta,
    public paciente?: IPaciente,
    public medico?: IMedico,
    public tipoDiagnostico?: ITipoDiagnostico,
    public estadoDiagnostico?: IEstadoDiagnostico,
  ) {
    this.activo = this.activo ?? false;
    this.esPrincipal = this.esPrincipal ?? false;
  }
}
