import { type IDiagnostico } from '@/shared/model/diagnostico.model';
import { type IEstadoTratamiento } from '@/shared/model/estado-tratamiento.model';

export interface ITratamiento {
  id?: number;
  codigo?: string;
  descripcion?: string;
  fechaInicio?: Date;
  fechaFin?: Date | null;
  observaciones?: string | null;
  fechaProximaRevision?: Date | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  diagnostico?: IDiagnostico;
  estadoTratamiento?: IEstadoTratamiento;
}

export class Tratamiento implements ITratamiento {
  constructor(
    public id?: number,
    public codigo?: string,
    public descripcion?: string,
    public fechaInicio?: Date,
    public fechaFin?: Date | null,
    public observaciones?: string | null,
    public fechaProximaRevision?: Date | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public diagnostico?: IDiagnostico,
    public estadoTratamiento?: IEstadoTratamiento,
  ) {
    this.activo = this.activo ?? false;
  }
}
